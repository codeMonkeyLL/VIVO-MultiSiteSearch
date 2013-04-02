package edu.cornell.mannlib.vivo.mms.linkedData;


import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * A class to use apache commons HttpClient to make
 * requests for RDF.
 *
 * Redirects are handled by HttpClient.
 *
 * TODO:
 * deal with connection refused
 * deal with timeouts
 * deal with character encoding
 */

public class HttpLinkedDataService implements LinkedDataService {
    Log logger = LogFactory.getLog(HttpLinkedDataService.class);

    HttpClient http;

    public HttpLinkedDataService(HttpClient http){
        this.http = http;
    }

    /**
     * Get RDF for URI and add to Model m.
     * @param uri
     * @param m may modified this method.
     * @throws Throws descriptive Exception if things go wrong.
     */
    @Override
	public void getLinkedData( String uri, Model m )
        throws Exception {
        logger.trace("getLinkedData "+uri);
        HttpGet get = new HttpGet(uri);
        get.setHeader("Accept", RDF_ACCEPT_HEADER);
        HttpResponse resp = http.execute( get );
        try{
            responseToModel(uri,m,resp);
        }catch(Exception ex){
            throw new Exception("could not get LD for " + uri + " " , ex);
        }finally{
            close( resp );
        }
    }


    protected void responseToModel(String uri, Model m, HttpResponse response)
        throws Exception {
        logger.trace( "got response "+response+" for "+uri);
        if( response == null )
            throw new Exception("HTTP response for " +uri+ " was null.");
        if( response != null &&
            response.getStatusLine() != null &&
            response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new Exception("could not get HTTP for " + uri +
                                " status: " + response.getStatusLine() );
        }

        HttpEntity  entity = response.getEntity();
        if (entity == null) {
            throw new Exception("could not get HttpEntity for " + uri +
                                " status: " + response.getStatusLine() );
        }else{
            try (InputStream instream = entity.getContent()){
                m.read(instream, "", getRDFType( entity ));
                instream.close();
            } catch (Exception ex ){
                throw new Exception("Could not parse RDF for " + uri
                    + " status: " + response.getStatusLine() + " "
                    + entity.getContentType() , ex );
            }
        }
    }

    protected String getRDFType( HttpEntity entity ) throws Exception{
        if( entity == null )
            throw new Exception("Could not identifiy content type: HttpResponse entity was null.");
        if( entity.getContentType() == null )
            throw new Exception("Could not identifiy content type: Content type was null.");
        if(entity.getContentType().getValue() == null )
            throw new Exception("Could not identifiy content type: "
                                +"value of entity.getContentType().getValue() was null");

        //TODO: better content type header parsing
        //TODO: handle character encoding, right now assuming UTF-8
        //strip charset=UTF-8 part, it gets ignored right now
        String contentType = entity.getContentType().getValue();
        if( contentType.contains(";") ){
            contentType = contentType.substring(0,contentType.indexOf(';'));
        }

        String type = HEADER_TO_JENASTR.get( contentType );
        if( type == null || type.trim().length() == 0 ){
            throw new Exception("Could not identifiy content type \""
                                +entity.getContentType().getValue()+"\"");
        }else{
            return type;
        }
    }    

    protected static final String RDF_ACCEPT_HEADER =
        "text/n3, text/rdf+n3, application/rdf+xml;q=0.9, text/turtle;q=0.8";
    
    private static final Map<String,String>HEADER_TO_JENASTR;
    static{
        Map<String,String> m = new HashMap<>();
        m.put("text/n3", "N3");
        m.put("text/rdf+n3", "N3");
        m.put("application/rdf+xml", "RDF/XML");
        m.put("text/turtle", "TURTLE");
        HEADER_TO_JENASTR = Collections.unmodifiableMap(m);
    }


    protected void close(  HttpResponse response ) {
        try{
            if( response != null ){
                HttpEntity entity = response.getEntity();
                if( entity != null ){
                    EntityUtils.consume(entity);
                }
            }
        }catch( Throwable th){
            logger.error("could not close entity",th);
        }
    }

}

