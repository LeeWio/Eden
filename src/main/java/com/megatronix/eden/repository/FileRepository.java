
package com.megatronix.eden.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.megatronix.eden.pojo.File;

public interface FileRepository extends JpaRepository<File, String> {

}
