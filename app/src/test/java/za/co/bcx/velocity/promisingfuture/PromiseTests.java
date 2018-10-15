package za.co.bcx.velocity.promisingfuture;

//this is from the compatibility library!
//not the standard one that is unavailable before a late Android release
import java9.util.concurrent.CompletableFuture;


import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import za.co.bcx.velocity.promisingfuture.service.GitHubService;

import static org.junit.Assert.fail;

public class PromiseTests {


    @Test
    public void can_list_github_user_repos_as_promise() {
        //This is a promise that we will return a List of GitHubService Repos later
        //when we are ready.
        CompletableFuture<List<GitHubService.Repo>> promiseRepoList = GitHubService.repos("google");


        assertNotNull("This promise is null", promiseRepoList);
        List<GitHubService.Repo> repos = null;
        try {
            repos = promiseRepoList.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Failed to deliver on the promise.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We were interrupted while waiting for the promise to be delivered");
        }


        for (GitHubService.Repo repo : repos) {
            System.out.println(repo.name + ": \""+ repo.description+ "\" ( has issues=" + repo.has_issues + ")");
        }

    }

    @Test
    public void can_list_repo_issues_as_promise() {
        //This is a promise that we will return a List of GitHubService issues later
        //when we are ready.
        CompletableFuture<List<GitHubService.Issue>> promiseIssueList = GitHubService.issues("google","acme");


        assertNotNull("This promise is null", promiseIssueList);
        List<GitHubService.Issue> issues = null;
        try {
            issues = promiseIssueList.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Failed to deliver on the promise.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We were interrupted while waiting for the promise to be delivered");
        }


        for (GitHubService.Issue issue : issues) {
            System.out.println(issue.title + ": "+ issue.state);
        }

    }

    @Test
    public void can_chain_promises() {
        //This is a promise that we will return a List of GitHubService Repos later
        //when we are ready.
        CompletableFuture<List<GitHubService.Repo>> promiseRepoList = GitHubService.repos("google");


        assertNotNull("This promise is null", promiseRepoList);
        List<GitHubService.Repo> repos = null;
        try {
            repos = promiseRepoList.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            fail("Failed to deliver on the promise.");
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("We were interrupted while waiting for the promise to be delivered");
        }

        HashMap<String,CompletableFuture<List<GitHubService.Issue>>> issueMap= new HashMap<String,CompletableFuture<List<GitHubService.Issue>>>();
        for (GitHubService.Repo repo : repos) {
            System.out.println(repo.name + ": \""+ repo.description+ "\" ( has issues=" + repo.has_issues + ")");
            //This is a promise that we will return a List of GitHubService issues later
            //when we are ready.
            CompletableFuture<List<GitHubService.Issue>> promiseIssueList = GitHubService.issues("google",repo.name);


            assertNotNull("This promise is null", promiseIssueList);
            issueMap.put(repo.name,promiseIssueList);



        }

        //we wait for all the promises to be ready to get without blocking
        for(CompletableFuture<List<GitHubService.Issue>> issueListPromise:issueMap.values()) {
            issueListPromise.join();
        }

        //we wait for all the promises to be ready to get without blocking
        for(CompletableFuture<List<GitHubService.Issue>> issueListPromise:issueMap.values()) {
            try {
                if(issueListPromise==null) {
                    continue;
                }
                for(GitHubService.Issue issue:issueListPromise.get()) {
                    if(issue==null) {
                        continue;
                    }
                    System.out.println(issue.title + ": "+ issue.state);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail("We were interrupted while waiting for the promise to be delivered");
            } catch (ExecutionException e) {
                e.printStackTrace();
                fail("Failed to deliver on the promise.");
            }
        }
    }

}
