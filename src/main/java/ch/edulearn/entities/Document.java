package ch.edulearn.entities;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.annotations.Identity;
import ch.edulearn.database.entity.annotations.NotNull;
import ch.edulearn.database.entity.annotations.Table;

import java.util.Objects;

@Table("document")
public class Document extends Entity {

    @Identity
    @Column("user_id")
    public int id;

    @NotNull
    @Column("title")
    public String title;

    @Column("description")
    public String description;

    public Document(int id, String title, String description) {

        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Document() {

    }

    @Override
    public String toString() {

        return "Document{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof Document)) return false;
        Document document = (Document) o;
        return id == document.id && Objects.equals(title, document.title) && Objects.equals(
                description,
                document.description
        );
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, title, description);
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

}
