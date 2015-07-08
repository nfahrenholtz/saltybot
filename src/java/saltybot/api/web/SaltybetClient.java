package saltybot.api.web;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import saltybot.api.common.*;
import saltybot.api.function.CheckedFunction;
import saltybot.api.excaption.SaltyException;

import java.util.*;

public class SaltybetClient {

    private final HttpHost host;
    private final CookieStore cookieStore;
    
    public SaltybetClient() {
        //host must have 'www' or the server will reject post requests
        host = new HttpHost("www.saltybet.com");

        cookieStore = new BasicCookieStore();
    }

    private <T> T request(HttpRequest request, CheckedFunction<HttpResponse, T> action) throws SaltyException {
        
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        final HttpClientContext context = HttpClientContext.create();

        context.setCookieStore(cookieStore);
        
        //the user agent should to mimic a popular web browser's
        //in this case the user agent is mimicking chrome's (40.0.2214.111)
        clientBuilder.setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
        
        try (final CloseableHttpClient client = clientBuilder.build()) {
            try (final CloseableHttpResponse response = client.execute(host, request, context)) {
                return action.apply(response);
            }
        } catch (final Throwable e) {
            throw new SaltyException("Failure to perform http request.", e);
        }
    }
    
    public boolean authenticate(final Credentials credentials) throws SaltyException {

        final HttpPost post = new HttpPost("/authenticate?signin=1");

        final Collection<NameValuePair> form = new ArrayList<>(3);
        form.add(new BasicNameValuePair("email", credentials.getEmail()));
        form.add(new BasicNameValuePair("pword", credentials.getPassword()));
        form.add(new BasicNameValuePair("authenticate", "signin"));
        post.setEntity(new UrlEncodedFormEntity(form));

        return request(post, (response) -> {
            final Header location = response.getLastHeader("Location");
            final Integer code = response.getStatusLine().getStatusCode();
            final HttpEntity entity = response.getEntity();

            EntityUtils.consumeQuietly(entity);

            //the response code from saltybet.com/authenticate is a 302 (Moved Temporarily)
            //if the Location header does not contain the query parameter error then login was a success
            return code.equals(302) && !StringUtils.containsIgnoreCase(location.getValue(), "error");
        });
    }

    public boolean validate() {
        //ToDo: validate that the client is still authenticated with the server
        return true;
    }
    
    public Balance balance() throws SaltyException {

        final HttpGet get = new HttpGet("/");
        
        return request(get, (response) -> {
            final HttpEntity entity = response.getEntity();
            final String html = EntityUtils.toString(entity);
            final Document doc = Jsoup.parse(html);

            Elements status = doc.select("#status");
            String balance = status.select("input#b").val();

            return new Balance(new Double(balance).intValue());
        });
    }
    
    public boolean bet(final Wager wager) throws SaltyException  {

        if (!wager.getPlayer().matches("player[12]")) {
            throw new SaltyException("Player value must be 'player1' or 'player2'.");
        }

        final HttpPost post = new HttpPost("/ajax_place_bet.php");

        final Collection<NameValuePair> form = new ArrayList<>(2);
        form.add(new BasicNameValuePair("selectedplayer", wager.getPlayer()));
        form.add(new BasicNameValuePair("wager", String.valueOf(wager.getWager())));
        post.setEntity(new UrlEncodedFormEntity(form));
        
        return request(post, (response) -> {
            final Integer code = response.getStatusLine().getStatusCode();
            final HttpEntity entity = response.getEntity();
            final String body = EntityUtils.toString(entity);

            //looks like place bet always returns a 200
            //a response of 1 seems to be successful, anything is a failure
            return code.equals(200) && StringUtils.containsIgnoreCase(body, "1");
        });
    }
    
    public State state() throws SaltyException {

        final HttpGet get = new HttpGet("/state.json");

        return request(get, (response) -> {
            final HttpEntity entity = response.getEntity();
            final String json = EntityUtils.toString(entity);
            final JSONObject object = new JSONObject(json);

            return State.create(object);
        });
    }
    
    public ZData zdata() throws SaltyException {

        final HttpGet get = new HttpGet("/zdata.json");
        
        return request(get, (response) -> {
            final HttpEntity entity = response.getEntity();
            final String json = EntityUtils.toString(entity);
            final JSONObject object = new JSONObject(json);

            return ZData.create(object);
        });
    }

    @Deprecated
    public String getZdata() throws SaltyException {

        final HttpGet get = new HttpGet("/zdata.json");

        return request(get, (response) -> {
            final HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        });
    }

    @Deprecated
    public <T> String getState() throws SaltyException {

        final HttpGet get = new HttpGet("/state.json");

        return request(get, (response) -> {
            final HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        });
    }
}
