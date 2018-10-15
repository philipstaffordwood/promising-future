package za.co.bcx.velocity.promisingfuture;

import java.io.IOException;

import java9.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class StackOverflowApiClient {

    /**
     * Lifted from https://github.com/bkhezry/Retrofit2-Example
     * https://github.com/bkhezry/Retrofit2-Example/blob/master/app/src/main/java/ir/bkhezry/retrofit2/Service/APIService.java
     */
    public interface APIService {
        @GET("questions")
        Call<Question> getQuestionsService(@Query("page") int page,
                                           @Query("pagesize") int pagesize,
                                           @Query("order") String order,
                                           @Query("sort") String sort,
                                           @Query("tagged") String tagged,
                                           @Query("site") String site);


    }

    private static final String TAG = "StackOverflowApiClient";

    public static CompletableFuture<Question> makeActiveTagQuery(int page, int pageSize, String order, String sort, String tagged, String site)  {
        //This is a promise we'll provide a Question value in the future
        final CompletableFuture<Question> completableFuture
                = new CompletableFuture<Question>();


        int pageVar = page;
        int pageSizeVar = pageSize;
        String orderVar = order;
        String sortVar = sort;
        String taggedVar = tagged;
        String siteVar = site;

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.stackexchange.com/2.2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService apiService = retrofit.create(APIService.class);

        //This is an implementation that, if executed produces
        //a future string asyncronously
        Runnable runner = new Runnable() {
            @Override
            public void run() {

                    Call<Question> call = apiService.getQuestionsService(pageVar, pageSizeVar, orderVar, sortVar, taggedVar, siteVar);

                    try {
                        Response<Question> response = call.execute();
                        //Here we make good on our promise
                        completableFuture.complete(response.body());
                    } catch (IOException e) {
                        e.printStackTrace();
                    };


            }
        };
        //we start executing it.
        runner.run();

        //we return our promise
        return completableFuture;




    }






}
