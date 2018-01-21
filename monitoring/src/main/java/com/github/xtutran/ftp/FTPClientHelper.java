/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.xtutran.ftp;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author xtutran
 */
public class FTPClientHelper {

//    public static String host = "69.125.12.12";
//    public static String hostFolder = "/report";
//    public static String username = "Adam";
//    public static String password = "Xifj39sky";
//    public static String subFolder = "";
//    final FTPClient client = new FTPClient();

    private final FTPClient ftp;
    private String server;


    public FTPClientHelper(String server) {
        this.server = server;
        this.ftp = new FTPClient();
    }

//    public FTPClientHelper(String _host, String _username, String _password,
//                           String _hostFolder, String _subFolder) {
//        host = _host;
//        username = _username;
//        password = _password;
//        hostFolder = _hostFolder;
//        subFolder = _subFolder;
////        client = new FTPClient();
//    }


//    private static void showServerReply(FTPClient ftpClient) {
//        String[] replies = ftpClient.getReplyStrings();
//        if (replies != null && replies.length > 0) {
//            for (String aReply : replies) {
//                System.out.println("SERVER: " + aReply);
//            }
//        }
//    }

    private boolean makeDirectories(FTPClient ftpClient, String dirPath) throws IOException {
        String[] pathElements = dirPath.split("/");
        if (pathElements.length > 0) {
            for (String singleDir : pathElements) {
                boolean existed = ftpClient.changeWorkingDirectory(singleDir);
                if (!existed) {
                    boolean created = ftpClient.makeDirectory(singleDir);
                    if (created) {
                        System.out.println("CREATED directory: " + singleDir);
                        ftpClient.changeWorkingDirectory(singleDir);
                    } else {
                        System.out.println("COULD NOT create directory: " + singleDir);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean login(String username, String password) {
        try {
            ftp.connect(server);

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                System.err.println("FTP server refused connection.");
                return false;
            }

            return ftp.login(username, password);
        } catch (IOException e) {
            return false;
        }
    }

    public boolean uploadFile(String localFile, String remoteFile) {
        boolean result = false;
        try {
            if(ftp.isConnected() && ftp.isAvailable()) {
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                InputStream input = new FileInputStream(new File(localFile));

                String parentDir = FilenameUtils.getPathNoEndSeparator(remoteFile);
                makeDirectories(ftp, parentDir);
                ftp.storeFile(remoteFile, input);
                result = true;
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(ftp.isConnected()) {
                try {
                    ftp.disconnect();
                    ftp.logout();
                } catch(IOException ioe) {
                    // do nothing
                }
            }
        }
        return result;
    }

//    public boolean checkAccountInfo() throws Exception {
//        try {
//            client.connect(host);
//            return client.login(username, password);
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//
//    public void sendFile(File localFile, String hostFileName) throws Exception {
//        FileInputStream fis = null;
//
//        try {
//            client.connect(host);
//            client.login(username, password);
//
//            if (client.isConnected() && client.isAvailable()) {
//
//                if (!client.changeWorkingDirectory(this.hostFolder + "/" + this.subFolder)) {
//                    client.sendCommand("MKD " + this.hostFolder + "/" + this.subFolder);
//                    client.changeWorkingDirectory(this.hostFolder + "/" + this.subFolder);
//                }
//
//                client.sendCommand("TYPE I");
//                client.setFileType(FTP.BINARY_FILE_TYPE);
//
//                fis = new FileInputStream(localFile);
//
//                client.storeFile(hostFileName, fis);
//
//                client.logout();
//                fis.close();
//            } else {
//                System.err
//                        .println("Cannot connect to host, check username or password");
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//
//    }

    public static void main(String[] args) {

        FTPClientHelper helper = new FTPClientHelper("192.168.1.240");
        helper.login("adam", "Xifj39sky");
        helper.uploadFile("2018-01-21_11-23-24.jpg", "/upload123/teeee/2018-01-21_11-23-24.jpg");
    }

//    public String getFolder() {
//        return this.hostFolder;
//    }
//
//    public void setFolder(String _hostFolder) {
//        this.hostFolder = _hostFolder;
//    }
//
//    public String getHost() {
//        return host;
//    }
//
//    public void setHost(String _host) {
//        host = _host;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String _username) {
//        username = _username;
//    }
//
//    public String getPass() {
//        return password;
//    }
//
//    public void setPass(String _pass) {
//        password = _pass;
//    }

}


