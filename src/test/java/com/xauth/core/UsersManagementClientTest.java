package com.xauth.core;

import com.xauth.core.graphql.GraphQLException;
import com.xauth.core.mgmt.ManagementClient;
import com.xauth.core.mgmt.UsersManagementClient;
import com.xauth.core.types.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsersManagementClientTest {
    private ManagementClient managementClient;
    private UsersManagementClient usersManagementClient;

    private String email;
    private String password;
    private String username;
    private User user;

    private String randomString() {
        return Integer.toString(new Random().nextInt());
    }

    @Before
    public void before() throws IOException, GraphQLException {
        managementClient = new ManagementClient("7f74f487bc121542ad0c7e3d", "cb6254521050caf857855214bc9dba98");
        managementClient.setHost("http://localhost:7001");
        usersManagementClient = managementClient.users();

        managementClient.requestToken().execute();

        email = randomString() + "@gmail.com";
        password = "123";
        username = "gmail" + randomString();
        user = usersManagementClient.create(new CreateUserInput()
                .withEmail(email)
                .withPassword(password)
                .withUsername(username)
                .build()).execute();
    }

    public void after() throws IOException, GraphQLException {
        if (user == null) return;
        usersManagementClient.delete(user.getId()).execute();
    }

    @Test
    public void list() throws IOException, GraphQLException {
        PaginatedUsers users = usersManagementClient.list().execute();
        Assert.assertTrue(users.getTotalCount() > 0);
    }

    @Test
    public void create() throws IOException, GraphQLException {
        user=usersManagementClient.create(new CreateUserInput().withUsername("jjjj")).execute();
    }

    @Test
    public void update() throws IOException, GraphQLException {
        User result = usersManagementClient.update(user.getId(), new UpdateUserInput().withNickname("nickname")).execute();
        Assert.assertEquals(result.getNickname(), "nickname");
    }

    @Test
    public void detail() throws IOException, GraphQLException {
        User result = usersManagementClient.detail(user.getId()).execute();
        Assert.assertEquals(result.getEmail(), email);
    }

    @Test
    public void search() throws IOException, GraphQLException {
        String query = "gmail";
        PaginatedUsers users = usersManagementClient.search(query).execute();
        Assert.assertTrue(users.getTotalCount() > 0);
    }

    @Test
    public void batch() throws IOException, GraphQLException {
        ArrayList<String> list = new ArrayList<>();
        list.add(user.getId());
        List<User> users = usersManagementClient.batch(list).execute();
        Assert.assertTrue(users.size() > 0);
    }

    @Test
    public void delete() throws IOException, GraphQLException {
        CommonMessage message = usersManagementClient.delete(user.getId()).execute();
        user = null;
       // Assert.assertEquals(message.getCode().intValue(), 200);
    }

    @Test
    public void deleteMany() throws IOException, GraphQLException {
        ArrayList<String> list = new ArrayList<>();
        list.add(user.getId());
        CommonMessage message = usersManagementClient.deleteMany(list).execute();
        user = null;
        Assert.assertEquals(message.getCode().intValue(), 200);
    }

    @Test
    public void refreshToken() throws IOException, GraphQLException {
        RefreshToken token = usersManagementClient.refreshToken(user.getId()).execute();
        Assert.assertNotNull(token.getToken());
    }

    @Test
    public void listRoles() throws IOException, GraphQLException {
        PaginatedRoles roles = usersManagementClient.listRoles(user.getId()).execute();
        Assert.assertTrue(roles.getTotalCount() == 0);
    }

    @Test
    public void listPolicies() throws IOException, GraphQLException {
        PaginatedPolicyAssignments result = usersManagementClient.listPolicies(user.getId()).execute();
        Assert.assertTrue(result.getTotalCount() == 0);
    }

    @Test
    public void checkLogin() throws IOException, GraphQLException {
        System.out.println(managementClient.checkLoginStatus(new CheckLoginStatusParam()).execute());
    }
}
