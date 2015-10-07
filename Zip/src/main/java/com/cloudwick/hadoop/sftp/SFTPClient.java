package com.cloudwick.hadoop.sftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

public class SFTPClient {

	private ChannelSftp sftpChannel;
	private Session session; 
	
	public SFTPClient(Properties prop) throws JSchException {
		JSch jsch = new JSch();
		 
		Session session = jsch.getSession(prop.getProperty("source-user"),
				prop.getProperty("source-host"),
				Integer.parseInt(prop.getProperty("source-port", "22")));

		session.setPassword(prop.getProperty("source-password"));
		Properties sftpConf = new Properties();
		sftpConf.put("StrictHostKeyChecking", "no");
		session.setConfig(sftpConf);
		session.connect();
		this.session = session;
		System.out.println("session connected successfully");
		Channel channel = session.openChannel("sftp");
		channel.connect();
		this.sftpChannel = (ChannelSftp) channel;
		System.out.println("channel opened successfully");
	}

	public void close() {
		if ((session != null) && (session.isConnected()))
			session.disconnect();
	}
	
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}

	//return list of zip file names under source-directory
	public List<String> getZipFileNames(Properties prop)
			throws SftpException, IOException { 
		List<String> zipFileNames = new ArrayList<String>();
		String sourceFolderPath = prop.getProperty("source-directory");
		this.sftpChannel.cd(sourceFolderPath);
		SftpATTRS sourceDirAttrs = this.sftpChannel.lstat(sourceFolderPath);
		if (!sourceDirAttrs.isDir())
			throw new IOException("Source path is not a direcotry: "
					+ sourceFolderPath);

		for (Object entry : this.sftpChannel.ls(sourceFolderPath)) {
			if (entry instanceof com.jcraft.jsch.ChannelSftp.LsEntry) {
				String entryName = ((com.jcraft.jsch.ChannelSftp.LsEntry) entry)
						.getFilename();
				SftpATTRS entryAttrs = ((com.jcraft.jsch.ChannelSftp.LsEntry) entry)
						.getAttrs();
				if (!entryAttrs.isDir() && entryName.endsWith(".zip")) {
					zipFileNames.add(entryName.toString());
				}
			}
		}
		return zipFileNames;
	}
	
	//return inputstream for zip file under source-directory
	public InputStream getInputStream(Properties prop,String sourceName) throws SftpException { 
		return this.sftpChannel.get(prop.getProperty("source-directory") + sourceName);
	}

}
