package ch.edulearn.entities;

import ch.edulearn.database.entity.Entity;
import ch.edulearn.database.entity.annotations.Column;
import ch.edulearn.database.entity.annotations.Searchable;
import ch.edulearn.database.entity.annotations.Table;

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

}
