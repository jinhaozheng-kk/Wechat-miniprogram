package com.example.service;

import com.example.common.BusinessException;
import com.example.config.HuaxinProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件上传业务
 * <p>
 * 文件按日期目录存放：upload/yyyyMMdd/<uuid>.<ext>
 * 返回值是相对 upload 根目录的访问路径，前端拼接 baseURL 后即可访问。
 */
@Service
public class UploadService {

    private static final DateTimeFormatter DATE_DIR = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final HuaxinProperties properties;

    @Autowired
    public UploadService(HuaxinProperties properties) {
        this.properties = properties;
    }

    /**
     * 保存上传文件
     *
     * @return 形如 /upload/20260504/abc-xxx.jpg 的可访问路径
     */
    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件为空");
        }
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null) {
            int dot = original.lastIndexOf('.');
            if (dot >= 0) {
                ext = original.substring(dot).toLowerCase();
            }
        }
        // 仅放行常见图片类型，避免任意文件上传风险
        if (!ext.matches("\\.(jpg|jpeg|png|webp|gif)")) {
            throw new BusinessException("仅支持上传 jpg/jpeg/png/webp/gif 图片");
        }

        String dateDir = LocalDate.now().format(DATE_DIR);
        String filename = UUID.randomUUID().toString().replace("-", "") + ext;

        Path baseDir = Paths.get(properties.getUploadDir()).toAbsolutePath().normalize();
        Path targetDir = baseDir.resolve(dateDir);
        Path targetFile = targetDir.resolve(filename);
        try {
            Files.createDirectories(targetDir);
            file.transferTo(targetFile.toFile());
        } catch (IOException e) {
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }

        // 拼出对外访问 URL（与 WebMvcConfig 中静态资源映射一致）
        return properties.getUploadUrlPrefix() + "/" + dateDir + "/" + filename;
    }
}
