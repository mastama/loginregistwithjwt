package com.example.chrombit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BlogDto {

    private UUID id;
    private String title;
    private String body;
    private String slug;
}
