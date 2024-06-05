package org.example.authentication.model.file;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@MappedSuperclass
public class FileData {
    @SequenceGenerator(
            name = "file_sequence",
            sequenceName = "file_sequence",
            allocationSize = 1

    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_sequence"
    )
    @Column(name = "id")
    protected long id;
    @Column(name ="name")
    protected String name;
    @Column(name ="type")
    protected String type;
    @Column(name ="file_path")
    protected String filePath;
}