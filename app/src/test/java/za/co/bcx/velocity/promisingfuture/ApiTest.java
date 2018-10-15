package za.co.bcx.velocity.promisingfuture;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import za.co.bcx.velocity.promisingfuture.service.GitHubService;

import static org.junit.Assert.*;

/**
 * We can make REST calls using Retrofit
 *
 * @See <a href="https://square.github.io/retrofit/">Retrofit</a>
 *
 */
public class ApiTest {

    public static final String API_URL = "https://api.github.com";
    private Retrofit retrofit;
    private GitHubService.GitHub github;

    @Before
    public void before() {
        //from https://github.com/square/retrofit/blob/master/samples/src/main/java/com/example/retrofit/SimpleService.java
        // Create a very simple REST adapter which points the GitHub API.
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GitHub API interface.
        github = retrofit.create(GitHubService.GitHub.class);
    }
    @Test
    public void can_make_rest_call_with_unirest() {


            // Fetch and print a list of the contributors to the library.
        List<GitHubService.Contributor> contributors = null;
        try {
            // Create a call instance for looking up Retrofit contributors.
            Call<List<GitHubService.Contributor>> call = github.contributors("square", "retrofit");

            contributors = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            fail("The rest call failed!");
        }
        for (GitHubService.Contributor contributor : contributors) {
                System.out.println(contributor.login + " (" + contributor.contributions + ")");
            }

    }

    @Test
    public void can_list_github_project_issues() {

        List<GitHubService.Issue> issues = null;
        try {

            Call<List<GitHubService.Issue>> call = github.issues("square", "retrofit");
            issues = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            fail("The rest call failed!");
        }
        for (GitHubService.Issue issue : issues) {
            System.out.println(issue.title + " (" + issue.state + ")");
        }

    }

    @Test
    public void can_list_github_user_repos() {


        List<GitHubService.Repo> repos = null;
        try {

            Call<List<GitHubService.Repo>> call = github.repos("google");


              repos = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            fail("The rest call failed!");
        }
        for (GitHubService.Repo repo : repos) {
            System.out.println(repo.name + ": \""+ repo.description+ "\" ( has issues=" + repo.has_issues + ")");
        }

    }

    @Test
    public void can_chain_api_calls() {
        String owner = "google";
        List<GitHubService.Repo> repos = null;
        try {

            Call<List<GitHubService.Repo>> call = github.repos(owner);
            repos = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            fail("The rest call failed!");
        }
        for (GitHubService.Repo repo : repos) {
            System.out.println("Repository "+repo.name + ": \""+ repo.description+ "\" ( has issues=" + repo.has_issues + ")");
            List<GitHubService.Issue> issues = null;
            try {

                Call<List<GitHubService.Issue>> call = github.issues(owner, repo.name);
                issues = call.execute().body();
            } catch (IOException e) {
                e.printStackTrace();
                fail("The rest call failed!");
            }
            if(issues==null) {
                continue;
            }
            for (GitHubService.Issue issue : issues) {
                System.out.println("  Issue "+issue.title + " (" + issue.state + ")");
            }
        }
    }
}