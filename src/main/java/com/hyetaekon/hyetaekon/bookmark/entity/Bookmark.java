package com.hyetaekon.hyetaekon.bookmark.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
            @Index(name = "idx_id", columnList = "id")
        }
)
public class Bookmark {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


}