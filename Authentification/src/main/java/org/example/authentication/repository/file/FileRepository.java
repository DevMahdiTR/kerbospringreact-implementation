package org.example.authentication.repository.file;

import org.example.authentication.model.file.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@NoRepositoryBean
@Transactional(readOnly = true)
public interface FileRepository <T extends FileData> extends JpaRepository<T, Long> {

    default Optional<T> findFileById(final long id){
        return findAll().stream()
                .filter(f -> f.getId() == id)
                .findFirst();
    }


}
