package com.example.sideproject_board.domain;

import jakarta.persistence.*;
import lombok.Cleanup;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
abstract class BaseTimeEntity {

  @Column (name = "created_date", nullable = false)
  @CreatedDate
    private String createdDate;

  @Column(name = "modified_date" ,nullable = false)
  @LastModifiedDate
    private String modifiedDate;

  @PrePersist // 해당 엔티티를 저장하기 전에 실행
    public void onPrePersist(){
      this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.mm.dd"));
      this.modifiedDate = this.createdDate;
  }
  @PreUpdate // 해당 엔티티를 업데이트 하기 이전에 실행.
    public void onPreUpdate(){
      this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
  }
}
