package za.co.bcx.velocity.promisingfuture;

import android.util.Log;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class StackOverflowApiClient {
    private static final String TAG = "StackOverflowApiClient";

    public void makeActiveTagQuery() throws UnirestException {
        ///2.2/tags?fromdate=1536796800&todate=1536883200&order=desc&sort=popular&site=stackoverflow
        String resultBody = Unirest.get("http://api.stackexchange.com/2.2/tags").
//                header("accept","applciation/json").
                field("fromdate","1536796800").
                field("todate","1536883200").
                field("order","desc").
                field("sort","popular").
                field("site","stackoverflow").asString().getBody();
        Log.v(TAG,resultBody);

    }
}
