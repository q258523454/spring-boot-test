package com.ftp_util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Date: 2019-05-27
 * @Version 1.0
 */
public class FTPUtilImpl implements FTPUtil {
    private static Logger log = LoggerFactory.getLogger(FTPUtilImpl.class);


    /***
     * 获取path下所有的文件名
     * @param ftpClient
     * @param folderPath
     * @return
     */
    @Override
    public List<String> getFolderFileLists(FTPClient ftpClient, String folderPath) {
        List<String> fileNameLists = new ArrayList<>();
        FTPFile[] ftpFiles = null;
        try {
            /**
             * 【强制】
             * ftpClient.listFiles(folderPath)之前一定要调用FTPClient.enterLocalPassiveMode()
             * 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
             * 原因: ftp server可能每次开启不同的端口来传输数据，但是在linux上，
             * 由于安全限制，可能某些端口没有开启，造成linux无法读取到目录下的文件列表
             */
            ftpClient.enterLocalPassiveMode();
            ftpFiles = ftpClient.listFiles(folderPath);
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isFile()) {
                    fileNameLists.add(ftpFile.getName());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("FTP获取:[{}]目录下文件名异常！", folderPath);
        }
        return fileNameLists;
    }


    /**
     * 获取path下所有的文件名, 按照创建时间降序
     *
     * @param ftpClient
     * @param folderPath
     * @return Map<fileName, createTimeStamp>
     */
    @Override
    public Map<String, String> getFolderFileListsByCreateTimeDesc(FTPClient ftpClient, String folderPath) {
        Map<String, String> map = new HashMap<>();
        FTPFile[] ftpFiles = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            /**
             * 【强制】
             * ftpClient.listFiles(folderPath)之前一定要调用FTPClient.enterLocalPassiveMode()
             * 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据。
             * 原因: ftp server可能每次开启不同的端口来传输数据，但是在linux上，
             * 由于安全限制，可能某些端口没有开启，造成linux无法读取到目录下的文件列表
             */
            ftpFiles = ftpClient.listFiles(folderPath);
            for (FTPFile ftpFile : ftpFiles) {
                if (ftpFile.isFile()) {
                    Calendar ftpFileTimestamp = ftpFile.getTimestamp();
                    String ftpFileTimestampStr = simpleDateFormat.format(ftpFileTimestamp.getTime());
                    map.put(ftpFile.getName(), ftpFileTimestampStr);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("FTP获取:[{}]目录下文件名异常！", folderPath);
        }
        System.out.println("文件按时间降序前:");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("文件按时间降序后:");
        map = MapSortUtil.sortByValueDesc(map);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        return map;
    }

    @Override
    public boolean readTxtFile(FTPClient ftpClient, String filePath, String fileName) {
        try {
            // 切换目录
            ftpClient.changeWorkingDirectory(new String(filePath.getBytes("UTF-8"), "ISO-8859-1"));
            //设置FTP连接模式
            ftpClient.enterLocalPassiveMode();
            //获取指定目录下文件文件对象集合
            FTPFile files[] = ftpClient.listFiles();
            InputStream in = null;
            BufferedReader reader = null;
            if (fileName.endsWith(".txt") || fileName.endsWith(".DAT")) {
                in = ftpClient.retrieveFileStream(new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
                reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String temp;
                StringBuffer buffer = new StringBuffer(1024);
                while ((temp = reader.readLine()) != null) {
                    buffer.append(temp);
                }
                if (reader != null) {
                    reader.close();
                }
                if (in != null) {
                    in.close();
                }
                //ftpClient.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
                ftpClient.completePendingCommand();
                //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
                System.out.println(buffer.toString());
                return true;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.error("FTP获取:[{}]目录下的[{}]文件异常！", filePath, fileName);
        }
        return true;
    }

    /**
     * 遍历解析文件夹下所有文件
     *
     * @param folderPath 需要解析的的文件夹
     * @param ftpClient  FTPClient对象
     * @return
     */
    @Override
    public boolean readTxtFileByFolder(FTPClient ftpClient, String folderPath) {
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(new String(folderPath.getBytes("UTF-8"), "ISO-8859-1"));
            //设置FTP连接模式
            ftpClient.enterLocalPassiveMode();
            //获取指定目录下文件文件对象集合
            FTPFile files[] = ftpClient.listFiles();
            InputStream in = null;
            BufferedReader reader = null;
            for (FTPFile file : files) {
                //判断为txt文件则解析
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.endsWith(".txt")) {
                        in = ftpClient.retrieveFileStream(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String temp;
                        StringBuffer buffer = new StringBuffer();
                        while ((temp = reader.readLine()) != null) {
                            buffer.append(temp);
                        }
                        if (reader != null) {
                            reader.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        //ftpClient.retrieveFileStream使用了流，需要释放一下，不然会返回空指针
                        ftpClient.completePendingCommand();
                        //这里就把一个txt文件完整解析成了个字符串，就可以调用实际需要操作的方法
                        System.out.println(buffer.toString());
                    }
                }
                //判断为文件夹，递归
                if (file.isDirectory()) {
                    String path = folderPath + File.separator + file.getName();
                    readTxtFileByFolder(ftpClient, path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("文件解析失败");
        }
        return flag;
    }

    /**
     * 下载FTP下指定文件
     *
     * @param ftpClient FTPClient对象
     * @param filePath  FTP文件路径
     * @param fileName  文件名
     * @param downPath  下载保存的目录
     * @return
     */
    @Override
    public boolean downLoadFTP(FTPClient ftpClient, String filePath, String fileName,
                               String downPath) {
        // 默认失败
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftpClient.changeWorkingDirectory(filePath);
            // 设置被动模式
            ftpClient.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {
                    File downFile = new File(downPath + File.separator + file.getName());
                    OutputStream out = new FileOutputStream(downFile);
                    // 绑定输出流下载文件,需要设置编码集，不然可能出现文件为空的情况
                    flag = ftpClient.retrieveFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    // 下载成功删除文件,看项目需求
                    // ftpClient.deleteFile(new String(fileName.getBytes("UTF-8"),"ISO-8859-1"));
                    out.flush();
                    out.close();
                    if (flag) {
                        log.info("下载成功");
                    } else {
                        log.error("下载失败");
                    }
                }
            }

        } catch (Exception e) {
            log.error("下载失败");
        }

        return flag;
    }

    /**
     * FTP文件上传工具类
     *
     * @param ftpClient
     * @param localFilePath
     * @param ftpPath
     * @return
     */
    @Override
    public boolean uploadFile(FTPClient ftpClient, String localFilePath, String ftpPath) {
        boolean flag = false;
        InputStream in = null;
        try {
            // 设置PassiveMode传输
            ftpClient.enterLocalPassiveMode();
            //设置二进制传输，使用BINARY_FILE_TYPE，ASC容易造成文件损坏
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            //判断FPT目标文件夹时候存在不存在则创建
            if (!ftpClient.changeWorkingDirectory(ftpPath)) {
                ftpClient.makeDirectory(ftpPath);
            }
            //跳转目标目录
            ftpClient.changeWorkingDirectory(ftpPath);

            //上传文件
            File file = new File(localFilePath);
            in = new FileInputStream(file);
            String tempName = ftpPath + File.separator + file.getName();
            flag = ftpClient.storeFile(new String(tempName.getBytes("UTF-8"), "ISO-8859-1"), in);
            if (flag) {
                log.info("上传成功");
            } else {
                log.error("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传失败");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * FPT上文件的复制
     *
     * @param ftpClient FTPClient对象
     * @param olePath   原文件地址
     * @param newPath   新保存地址
     * @param fileName  文件名
     * @return
     */
    @Override
    public boolean copyFile(FTPClient ftpClient, String olePath, String newPath, String fileName) {
        boolean flag = false;

        try {
            // 跳转到文件目录
            ftpClient.changeWorkingDirectory(olePath);
            //设置连接模式，不设置会获取为空
            ftpClient.enterLocalPassiveMode();
            // 获取目录下文件集合
            FTPFile[] files = ftpClient.listFiles();
            ByteArrayInputStream in = null;
            ByteArrayOutputStream out = null;
            for (FTPFile file : files) {
                // 取得指定文件并下载
                if (file.getName().equals(fileName)) {

                    //读取文件，使用下载文件的方法把文件写入内存,绑定到out流上
                    out = new ByteArrayOutputStream();
                    ftpClient.retrieveFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), out);
                    in = new ByteArrayInputStream(out.toByteArray());
                    //创建新目录
                    ftpClient.makeDirectory(newPath);
                    //文件复制，先读，再写
                    //二进制
                    ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                    flag = ftpClient.storeFile(newPath + File.separator + (new String(file.getName().getBytes("UTF-8"), "ISO-8859-1")), in);
                    out.flush();
                    out.close();
                    in.close();
                    if (flag) {
                        log.info("转存成功");
                    } else {
                        log.error("复制失败");
                    }


                }
            }
        } catch (Exception e) {
            log.error("复制失败");
        }
        return flag;
    }

    /**
     * 实现文件的移动，这里做的是一个文件夹下的所有内容移动到新的文件，
     * 如果要做指定文件移动，加个判断判断文件名
     * 如果不需要移动，只是需要文件重命名，可以使用ftp.rename(oleName,newName)
     *
     * @param ftpClient
     * @param oldPath
     * @param newPath
     * @return
     */
    @Override
    public boolean moveFile(FTPClient ftpClient, String oldPath, String newPath) {
        boolean flag = false;

        try {
            ftpClient.changeWorkingDirectory(oldPath);
            ftpClient.enterLocalPassiveMode();
            // 获取文件数组
            FTPFile[] files = ftpClient.listFiles();
            // 新文件夹不存在则创建
            if (!ftpClient.changeWorkingDirectory(newPath)) {
                ftpClient.makeDirectory(newPath);
            }
            // 回到原有工作目录
            ftpClient.changeWorkingDirectory(oldPath);
            for (FTPFile file : files) {
                // 转存目录
                flag = ftpClient.rename(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"), newPath + "/" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                if (flag) {
                    log.info(file.getName() + "移动成功");
                } else {
                    log.error(file.getName() + "移动失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("移动文件失败");
        }
        return flag;
    }

    /**
     * 删除FTP上指定文件夹下文件及其子文件方法，添加了对中文目录的支持
     *
     * @param ftpClient FTPClient对象
     * @param FtpFolder 需要删除的文件夹
     * @return
     */
    @Override
    public boolean deleteByFolder(FTPClient ftpClient, String FtpFolder) {
        boolean flag = false;
        try {
            ftpClient.changeWorkingDirectory(new String(FtpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            ftpClient.enterLocalPassiveMode();
            FTPFile[] files = ftpClient.listFiles();
            for (FTPFile file : files) {
                //判断为文件则删除
                if (file.isFile()) {
                    ftpClient.deleteFile(new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));
                }
                //判断是文件夹
                if (file.isDirectory()) {
                    String childPath = FtpFolder + File.separator + file.getName();
                    //递归删除子文件夹
                    deleteByFolder(ftpClient, childPath);
                }
            }
            //循环完成后删除文件夹
            flag = ftpClient.removeDirectory(new String(FtpFolder.getBytes("UTF-8"), "ISO-8859-1"));
            if (flag) {
                log.info(FtpFolder + "文件夹删除成功");
            } else {
                log.error(FtpFolder + "文件夹删除成功");
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除失败");
        }
        return flag;

    }
}
