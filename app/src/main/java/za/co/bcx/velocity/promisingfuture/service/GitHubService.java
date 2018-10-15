package za.co.bcx.velocity.promisingfuture.service;

import java9.util.concurrent.CompletableFuture;

import java.io.IOException;
import java.util.List;


import retrofit2.Call;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * From https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java
 */
public final class GitHubService {
    public static final String API_URL = "https://api.github.com";




    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }
    }

    //https://developer.github.com/v3/issues/#response-1
    public static class Issue {
        public final String state;

        public final String title;

        public final String body;

        public Issue(String state, String title, String body) {
            this.state = state;
            this.title = title;
            this.body = body;
        }

    }

    public interface GitHub {
        @GET("/repos/{owner}/{repo}/contributors")
        Call<List<Contributor>> contributors(
                @Path("owner") String owner,
                @Path("repo") String repo);

        //https://developer.github.com/v3/issues/#list-issues-for-a-repository
        @GET("/repos/{owner}/{repo}/issues")
        Call<List<Issue>> issues(
                @Path("owner") String owner,
                @Path("repo") String repo);
        //https://developer.github.com/v3/repos/#list-user-repositories
        @GET("/users/{owner}/repos")
        Call<List<Repo>> repos(
                @Path("owner") String owner);
    }


    public class Repo {
        public String name;
        public String description;
        public boolean has_issues;

        public Repo(String name, String description, boolean has_issues) {
            this.name = name;
            this.description = description;
            this.has_issues = has_issues;
        }
    }

    public static CompletableFuture<List<Repo>> repos(String owner) {
        //This is a promise that we will deliver a List of GitHubService.Repos later
        CompletableFuture<List<GitHubService.Repo>> promiseRepoList =
                new CompletableFuture<List<GitHubService.Repo>>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService.GitHub github = retrofit.create(GitHubService.GitHub.class);

        //This is an implementation that, if executed produces
        //a a List of GitHubService.Repos asyncronously
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                try {
                    //here we will work at delivering on the promise
                    Call<List<GitHubService.Repo>> call = github.repos(owner);

                    List<GitHubService.Repo> repos = call.execute().body();

                    //here we will return the value we promised
                    promiseRepoList.complete(repos);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        //we start executing it.
        runner.run();

        //we return our promise (as we are busy delivering on it)
        return promiseRepoList;


    }

    public static CompletableFuture<List<Issue>> issues(String owner, String repo) {
        //This is a promise that we will deliver a List of GitHubService.Repos later
        CompletableFuture<List<GitHubService.Issue>> promiseIssueList =
                new CompletableFuture<List<GitHubService.Issue>>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GitHubService.GitHub github = retrofit.create(GitHubService.GitHub.class);

        //This is an implementation that, if executed produces
        //a a List of GitHubService.Repos asyncronously
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                try {
                    //here we will work at delivering on the promise
                    Call<List<GitHubService.Issue>> call = github.issues(owner,repo);

                    List<GitHubService.Issue> issues = call.execute().body();

                    //here we will return the value we promised
                    promiseIssueList.complete(issues);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        //we start executing it.
        new Thread(runner).start();

        //we return our promise (as we are busy delivering on it)
        return promiseIssueList;
    }
}