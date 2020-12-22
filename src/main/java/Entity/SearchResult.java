package Entity;

import database.annotations.Column;
import database.annotations.Searchable;
import database.annotations.Table;
import database.entity.Entity;

import java.sql.ResultSet;

/**
 * Represent a search result, for the full text search mechanism
 */
@Table("full_text_search_index")
public class SearchResult extends Entity {

    /**
     * The human readable type of the search result (Utilisateur, Cours...)
     */
    @Searchable
    @Column("type")
    public String type;

    /**
     * The title of the resource represented by
     * the search result (username, lesson name)
     */
    @Searchable
    @Column("title")
    public String title;

    /**
     * The description of the resource represented by the search result.
     * Possibly not present.
     */
    @Searchable
    @Column("description")
    public String description;

    /**s
     * The resource table name, for retrieving details infos
     */
    @Column("resource_table_name")
    public String resourceTable;

    /**
     * The id of the resource represented by the search result
     */
    @Column("resource_id")
    public String resourceId;

    public SearchResult(ResultSet dbResults) {

        super(dbResults);
    }

}
