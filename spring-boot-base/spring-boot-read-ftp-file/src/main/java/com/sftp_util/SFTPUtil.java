package com.sftp_util;

import com.jcraft.jsch.*;
import com.ftp_util.MapSortUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Date: 2019-06-19
 * @Version 1.0
 */
public class SFTPUtil {

    private final static Logger log = LoggerFactory.getLogger(SFTPUtil.class);

    /**
     * 列出目录下的文件
     */
    public static List<String> getFileListByPath(ChannelSftp sftp, String path) throws SftpException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List fileNameList = new ArrayList();
        sftp.cd(path);
        Vector vector = sftp.ls(path);
        for (int i = 0; i < vector.size(); i++) {
            if (vector.get(i) instanceof ChannelSftp.LsEntry) {
                ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.get(i);
                String fileName = lsEntry.getFilename();
                int mtime = lsEntry.getAttrs().getMTime();
                Date date = new Date(mtime * 1000L);
                String fileCreateTime = simpleDateFormat.format(date);
                if (".".equals(fileName) || "..".equals(fileName)) {
                    continue;
                }
                log.info("filename is {},mtime is {},file create time is {}.", fileName, mtime, fileCreateTime);
                fileNameList.add(fileName);
            }
        }
        return fileNameList;
    }

    public static void readTxtDataUTF8(ChannelSftp sftp, String file) throws SftpException, IOException {
        InputStream in = sftp.get(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        if (reader != null) {
            reader.close();
        }
        if (in != null) {
            in.close();
        }
    }

    public static void readTxtDataGBK(ChannelSftp sftp, String file) throws SftpException, IOException {
        InputStream in = sftp.get(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in,"GBK"));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        if (reader != null) {
            reader.close();
        }
        if (in != null) {
            in.close();
        }
    }


    //   获取path下所有的文件名(指定前缀), 按照创建时间降序
    public static Map<String, String> getFolderFileListsByCreateTimeDesc(ChannelSftp sftp, String folderPath, String filePrefix) {
        log.info("获取{}下所有的文件名(指定前缀), 按照创建时间降序", folderPath);
        Map<String, String> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            sftp.cd(folderPath);
            Vector vector = sftp.ls(folderPath);
            for (int i = 0; i < vector.size(); i++) {
                if (vector.get(i) instanceof ChannelSftp.LsEntry) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.get(i);
                    String fileName = lsEntry.getFilename();
                    if (".".equals(fileName) || "..".equals(fileName)) {
                        continue;
                    }
                    // 不是文件目录
                    if (!lsEntry.getAttrs().isDir() && fileName.startsWith(filePrefix)) {
                        int mtime = lsEntry.getAttrs().getMTime();
                        Date date = new Date(mtime * 1000L);
                        String fileCreateTime = simpleDateFormat.format(date);
                        map.put(fileName, fileCreateTime);
                    }
                    map = MapSortUtil.sortByValueDesc(map);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("SFTP获取:[{}]目录下文件名称异常！", folderPath);
        }
        return map;
    }


    /**
     * 获取path下所有文件目录((指定前缀), 按照创建时间降序
     */
    public static Map<String, String> getFolderFileDirectoryListsByCreateTimeDesc(ChannelSftp sftp, String folderPath, String directoryPrefix) {
        log.info("获取{}下所有的文件目录名, 按照创建时间降序", folderPath);
        Map<String, String> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            sftp.cd(folderPath);
            Vector vector = sftp.ls(folderPath);
            for (int i = 0; i < vector.size(); i++) {
                if (vector.get(i) instanceof ChannelSftp.LsEntry) {
                    ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) vector.get(i);
                    String fileName = lsEntry.getFilename();
                    if (".".equals(fileName) || "..".equals(fileName)) {
                        continue;
                    }
                    if (lsEntry.getAttrs().isDir() && fileName.startsWith(directoryPrefix)) {
                        int mtime = lsEntry.getAttrs().getMTime();
                        Date date = new Date(mtime * 1000L);
                        String fileCreateTime = simpleDateFormat.format(date);
                        map.put(fileName, fileCreateTime);
                    }
                    map = MapSortUtil.sortByValueDesc(map);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("SFTP获取:[{}]目录下文件夹名称异常！", folderPath);
        }
        return map;
    }


    public static String getFolderRecentDirectoryAndRecentFilePathByPrefix(ChannelSftp sftp, String folderPath, String directoryPrefix, String filePrefix) throws Exception {
        String ftpDirectoryNameRecentUpdate = "";
        String newFolderPath = "";
        String fileName = "";
        String fileToReadPath = "";
        try {
            Map<String, String> ftpDirectoryByCreateTimeDescMap = getFolderFileDirectoryListsByCreateTimeDesc(sftp, folderPath, directoryPrefix);

            if (null == ftpDirectoryByCreateTimeDescMap || 0 == ftpDirectoryByCreateTimeDescMap.size()) {
                throw new Exception("Path:" + folderPath + ",sftp directory lists read error");
            }
            ftpDirectoryNameRecentUpdate = ftpDirectoryByCreateTimeDescMap.entrySet().iterator().next().getKey();

            newFolderPath = folderPath + "/" + ftpDirectoryNameRecentUpdate;

            Map<String, String> ftpFileByCreateTimeDescMap = getFolderFileListsByCreateTimeDesc(sftp, newFolderPath, filePrefix);
            if (null == ftpFileByCreateTimeDescMap || 0 == ftpFileByCreateTimeDescMap.size()) {
                throw new Exception("Path:" + newFolderPath + ",sftp file name lists read error");
            }
            // 获取 ftpFileByCreateTimeDescMap(LinkedHashMap)的第一个元素
            fileName = ftpFileByCreateTimeDescMap.entrySet().iterator().next().getKey();

            fileToReadPath = newFolderPath + "/" + fileName;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("sftp directory read error: can not read directory name");
        }
        return fileToReadPath;
    }

}
