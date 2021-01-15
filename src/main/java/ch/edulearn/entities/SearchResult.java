package ch.edulearn.entities;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.annotations.Searchable;
import ch.edulearn.database.entity.annotations.Table;

import java.util.Objects;

/**
 * Represent a search result, for the full text search mechanism
 */
@Table("edulearn_full_text_search")
public class SearchResult extends Entity {

    /**
     * The human readable type of the search result (Utilisateur, Cours...)
     */
    @Searchable(3)
    @Column("type")
    public String type;

    /**
     * The title of the resource represented by
     * the search result (username, lesson name)
     */
    @Searchable(10)
    @Column("title")
    public String title;

    /**
     * The description of the resource represented by the search result.
     * Possibly not present.
     */
    @Searchable(5)
    @Column("description")
    public String description;

    /**
     * s
     * The resource table name, for retrieving details infos
     */
    @Column("resource_table_name")
    public String resourceTable;

    /**
     * The id of the resource represented by the search result
     */
    @Column("resource_id")
    public int resourceId;

    public SearchResult(String type, String title, String description, String resourceTable, int resourceId) {

        this.type = type;
        this.title = title;
        this.description = description;
        this.resourceTable = resourceTable;
        this.resourceId = resourceId;
    }

    public SearchResult() {

    }

    @Override
    public String toString() {

        return "SearchResult{" +
                "type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", resourceTable='" + resourceTable + '\'' +
                ", resourceId=" + resourceId +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof SearchResult)) return false;
        SearchResult that = (SearchResult) o;
        return resourceId == that.resourceId && Objects.equals(type, that.type) && Objects.equals(
                title,
                that.title
        ) && Objects.equals(description, that.description) && Objects.equals(
                resourceTable,
                that.resourceTable
        );
    }

    @Override
    public int hashCode() {

        return Objects.hash(type, title, description, resourceTable, resourceId);
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
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

    public String getResourceTable() {

        return resourceTable;
    }

    public void setResourceTable(String resourceTable) {

        this.resourceTable = resourceTable;
    }

    public int getResourceId() {

        return resourceId;
    }

    public void setResourceId(int resourceId) {

        this.resourceId = resourceId;
    }

}
