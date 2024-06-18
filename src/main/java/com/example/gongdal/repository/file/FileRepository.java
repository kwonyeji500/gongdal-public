package com.example.gongdal.repository.file;

import com.example.gongdal.entity.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
