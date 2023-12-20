package com.example.testTask.Repositories;

import com.example.testTask.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface CategoryRepo extends JpaRepository<Category, Long> {

//    List<Category> findByCategoryParentLikeIgnoreCase(String parent);

//    List<Category> findByCategoryChildLikeIgnoreCase(String child);

//    void deleteByCategoryParentLikeIgnoreCase(String parent);

//    void deleteByCategoryChildLikeIgnoreCase(String child);


}
