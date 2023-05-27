package com.example.mediaServices.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Media {

    @Id
    private String id;

    private String name;

    private TypeMedia type;

    private byte[] data;

    public Media(String name, TypeMedia type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
