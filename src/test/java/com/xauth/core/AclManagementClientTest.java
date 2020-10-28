package com.xauth.core;

import com.xauth.core.graphql.GraphQLException;
import com.xauth.core.mgmt.AclManagementClient;
import com.xauth.core.mgmt.ManagementClient;
import com.xauth.core.mgmt.RolesManagementClient;
import com.xauth.core.types.CommonMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

public class AclManagementClientTest {
    private ManagementClient managementClient;
    private AclManagementClient aclManagementClient;

    private String randomString() {
        return Integer.toString(new Random().nextInt());
    }

    @Before
    public void before() throws IOException, GraphQLException {
        managementClient = new ManagementClient("59f86b4832eb28071bdd9214", "4b880fff06b080f154ee48c9e689a541");
        managementClient.setHost("http://localhost:3000");
        aclManagementClient = managementClient.acl();

        managementClient.requestToken().execute();
    }

    @Test
    public void allow() throws IOException, GraphQLException {
        CommonMessage message = aclManagementClient.allow("resource id", "action id").execute();
        Assert.assertEquals(message.getCode().intValue(), 200);
    }

    @Test
    public void isAllowed() throws IOException, GraphQLException {
        boolean b = aclManagementClient.isAllowed("user id", "resource id", "action id").execute();
        Assert.assertTrue(b);
    }
}
