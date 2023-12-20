package com.example.testTask.Entity;


import jakarta.persistence.*;
//import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(name= "category_parent")

    private String CategoryParent;


    @Column(name = "category_child")
    private String CategoryChild;

    @Override
    public String toString() {
        return
                "id= " + id + "\n" +
                "Category " + CategoryParent + "\n" +
                        "|" +"\n" +
                        "|"+
                        "--"+
                "CategoryChild= " + CategoryChild + "\n";
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryParent() {
        return CategoryParent;
    }

    public void setCategoryParent(String CategoryParent) {
        this.CategoryParent = CategoryParent;
    }

    public String getCategoryChild() {
        return CategoryChild;
    }

    public void setCategoryChild(String CategoryChild) {
        this.CategoryChild = CategoryChild;
    }


}
