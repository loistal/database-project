package ch.epfl.dbms;

/**
 * Created by Gianni on 06.06.2016.
 */
public class Query {
    private String[] queries = {"SELECT EXTRACT (YEAR FROM Publications.publication_date), COUNT(Publications.publication_date)\n" +
            "FROM Publications\n" +
            "GROUP BY EXTRACT (YEAR FROM Publications.publication_date)\n",

            "SELECT authors_number_publications.a_name\n" +
                    "FROM (\n" +
                    "    SELECT Authors.author_id, Authors.a_name, COUNT(*) as pubs\n" +
                    "    FROM Authors, Publications, Publication_authors\n" +
                    "    WHERE Publication_authors.author_id = Authors.author_id AND\n" +
                    "          Publication_authors.publication_id = Publications.publication_id\n" +
                    "    GROUP BY Authors.author_id, Authors.a_name\n" +
                    "    ORDER BY pubs DESC\n" +
                    ") authors_number_publications\n" +
                    "WHERE ROWNUM <= 10",

            "SELECT Authors.a_name\n" +
                    "FROM Authors\n" +
                    "WHERE Authors.birthdate IN (\n" +
                    "        SELECT MAX(Authors.birthdate)\n" +
                    "        FROM Authors, Publications, Publication_authors\n" +
                    "        WHERE Publication_authors.author_id = Authors.author_id AND\n" +
                    "        Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "        EXTRACT (YEAR FROM Publications.publication_date) = 2010\n" +
                    "\n" +
                    "    ) OR\n" +
                    "    Authors.birthdate IN (\n" +
                    "      SELECT MIN(Authors.birthdate)\n" +
                    "        FROM Authors, Publications, Publication_authors\n" +
                    "        WHERE Publication_authors.author_id = Authors.author_id AND\n" +
                    "        Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "        EXTRACT (YEAR FROM Publications.publication_date) = 2010\n" +
                    ")",

            "SELECT COUNT ( Publications.publication_id )\n" +
                    "    FROM Title, Publication_content, Publications\n" +
                    "    WHERE Title.title_graphic = 'YES' AND\n" +
                    "    Title.title_id = Publication_content.title_id AND\n" +
                    "    Publications.publication_id = Publication_content.publication_id AND\n" +
                    "    Publications.publication_pages < 50",

            "SELECT COUNT ( Publications.publication_id )\n" +
                    "    FROM Title, Publication_content, Publications\n" +
                    "    WHERE Title.title_graphic = 'YES' AND\n" +
                    "    Title.title_id = Publication_content.title_id AND\n" +
                    "    Publications.publication_id = Publication_content.publication_id AND\n" +
                    "    Publications.publication_pages < 100",

            "SELECT COUNT ( Publications.publication_id )\n" +
                    "    FROM Title, Publication_content, Publications\n" +
                    "    WHERE Title.title_graphic = 'YES' AND\n" +
                    "    Title.title_id = Publication_content.title_id AND\n" +
                    "    Publications.publication_id = Publication_content.publication_id AND\n" +
                    "    Publications.publication_pages >= 100",

            "SELECT Publisher.publisher_id , AVG(Publications.price_value)\n" +
                    "    FROM Publications, Publisher\n" +
                    "    WHERE Publisher.publisher_id = Publications.publisher_id AND\n" +
                    "          Publications.price_currency = '$'\n" +
                    "    GROUP BY Publisher.publisher_id",


            "SELECT author_name\n" +
                    "FROM (\n" +
                    "    SELECT Authors.author_id, Authors.a_name AS author_name, COUNT ( Title.title_id ) as science_fiction_per_author\n" +
                    "    FROM Title, Authors, Title_tags, Tags, Publication_authors, Publications, Publication_content\n" +
                    "    WHERE Title.title_id = Title_tags.title_id AND\n" +
                    "          Title_tags.tag_id = Tags.tag_id AND\n" +
                    "          Tags.tag_name LIKE '%science fiction%' AND\n" +
                    "          Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "          Authors.author_id = Publication_authors.author_id AND\n" +
                    "          Publications.publication_id = Publication_content.publication_id AND\n" +
                    "          Title.title_id = Publication_content.title_id\n" +
                    "    GROUP BY Authors.author_id, Authors.a_name\n" +
                    "    ORDER BY science_fiction_per_author DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM = 1",

            "SELECT num_awards.title_id , num_awards + num_reviews\n" +
                    "FROM\n" +
                    "  (\n" +
                    "    SELECT Title.title_id, COUNT(Title_award.award_id) as num_awards\n" +
                    "    FROM Title, Title_award\n" +
                    "    WHERE Title.title_id = Title_award.title_id\n" +
                    "    GROUP BY Title.title_id\n" +
                    "  ) num_awards,\n" +
                    "  (\n" +
                    "    SELECT Title.title_id , COUNT (Reviews.title_id) as num_reviews\n" +
                    "    FROM Title, Reviews\n" +
                    "    WHERE Title.title_id = Reviews.title_id\n" +
                    "    GROUP BY Title.title_id\n" +
                    "  ) num_reviews\n" +
                    "WHERE num_awards.title_id = num_reviews.title_id",


            // Third deliverable
            "SELECT mPublications.price_currency, AVG(mPublications.price_value) as average_price\n" +
                    "FROM Publications mPublications, Title, Publication_content\n" +
                    "WHERE Publication_content.title_id = Title.title_id AND\n" +
                    "      Publication_content.publication_id = mPublications.publication_id AND\n" +
                    "      Title.title_id IN (\n" +
                    "\n" +
                    "                SELECT title_id\n" +
                    "                FROM (\n" +
                    "                  SELECT Title.title_id, COUNT(Publications.publication_id) AS number_publications\n" +
                    "                  FROM Title, Publications, Publication_content\n" +
                    "                  WHERE Publication_content.title_id = Title.title_id AND\n" +
                    "                        Publication_content.publication_id = Publications.publication_id\n" +
                    "                  GROUP BY Title.title_id\n" +
                    "                  ORDER BY number_publications DESC )\n" +
                    "                WHERE ROWNUM = 1\n" +
                    "\n" +
                    ")\n" +
                    "GROUP BY mPublications.price_currency",

            "SELECT *\n" +
                    "FROM (\n" +
                    "  SELECT Title_series.title, COUNT(Title_award.AWARD_ID) AS number_awards\n" +
                    "  FROM Title_series, Title_award, Title\n" +
                    "  WHERE Title.series_id = Title_series.id AND\n" +
                    "        Title_award.title_id = Title.title_id\n" +
                    "  GROUP BY Title_series.title\n" +
                    "  ORDER BY number_awards DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM <= 10",

            "SELECT *\n" +
                    "FROM (\n" +
                    "SELECT Authors.a_name, COUNT(Awards.award_id) AS number_awards\n" +
                    "FROM Authors, Awards, Title_award, Title, Publication_content, Publications, Publication_authors\n" +
                    "WHERE Publication_content.publication_id = Publications.publication_id AND\n" +
                    "      Publication_content.title_id = Title.title_id AND\n" +
                    "      Publication_authors.author_id = Authors.author_id AND\n" +
                    "      Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "      Title_award.title_id = Title.title_id AND\n" +
                    "      Awards.AWARD_DATE >= Authors.DEATHDATE\n" +
                    "GROUP BY Authors.a_name\n" +
                    "ORDER BY number_awards DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM = 1",

            "SELECT *\n" +
                    "FROM (\n" +
                    "  SELECT Publisher.PUBLISHER_ID, COUNT(Publications.publication_id) as number_publications\n" +
                    "  FROM Publications, Publisher\n" +
                    "  WHERE EXTRACT ( YEAR FROM Publications.publication_date ) = 2000 AND\n" +
                    "        Publications.publisher_id = Publisher.publisher_id\n" +
                    "  GROUP BY Publisher.publisher_id\n" +
                    "  ORDER BY number_publications DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM <= 3",

            "SELECT *\n" +
                    "FROM (\n" +
                    "SELECT Title.title, COUNT(Reviews.title_id) as number_reviews\n" +
                    "FROM Title, Reviews, Publications, Publication_authors, Authors, Publication_content\n" +
                    "WHERE Authors.a_name = 'H. G. Wells' AND\n" +
                    "      Reviews.title_id = Title.title_id AND\n" +
                    "      Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "      Publication_authors.AUTHOR_ID = Authors.author_id AND\n" +
                    "      Publication_content.title_id = Title.TITLE_ID AND\n" +
                    "      Publication_content.publication_id = Publications.PUBLICATION_ID\n" +
                    "GROUP BY Title.title\n" +
                    "ORDER BY number_reviews DESC\n" +
                    ") WHERE ROWNUM = 2",

            "SELECT mLanguage.language_id, Title.title_type\n" +
                    "FROM Title, Languages mLanguage\n" +
                    "WHERE Title.title_type IN (\n" +
                    "\n" +
                    "  SELECT Title.title_type\n" +
                    "  FROM (\n" +
                    "    -- Title types and their number of translations\n" +
                    "    SELECT Title.title_type, COUNT(Title.title_type) number_translations\n" +
                    "    FROM Title\n" +
                    "    WHERE Title.title_translator IS NOT NULL AND\n" +
                    "          Title.language_id = mLanguage.language_id\n" +
                    "    GROUP BY Title.title_type\n" +
                    "    ORDER BY number_translations DESC\n" +
                    "  )\n" +
                    "  WHERE ROWNUM <= 3\n" +
                    "\n" +
                    ")",

            "SELECT EXTRACT ( YEAR FROM Publications.publication_date ), Publisher.publisher_id, AVG(Authors.author_id)\n" +
                    "FROM Publisher, Authors, Publications, Publication_authors\n" +
                    "WHERE Publications.PUBLICATION_ID = Publication_authors.publication_id AND\n" +
                    "      Publication_authors.author_id = Authors.author_id AND\n" +
                    "      Publications.publisher_id = Publisher.PUBLISHER_ID\n" +
                    "GROUP BY Publications.publication_date, Publisher.PUBLISHER_ID\n" +
                    "ORDER BY Publications.publication_date",

            "SELECT *\n" +
                    "FROM(\n" +
                    "      SELECT Publication_series.ps_id, COUNT(Publications.publication_id) as number_publications_with_award\n" +
                    "      FROM Publication_series, Publications, Awards, Title, Title_award, Publication_content\n" +
                    "      WHERE Publications.PUBLICATIOM_SERIES_ID = Publication_series.PS_ID AND\n" +
                    "            Publications.publication_id = Publication_content.publication_id AND\n" +
                    "            Title.title_id = Publication_content.title_id AND\n" +
                    "            Title_award.title_id = Title.title_id AND\n" +
                    "            Awards.AWARD_TITLE LIKE '%World Fantasy Award%' AND\n" +
                    "            Awards.AWARD_ID = Title_award.AWARD_ID\n" +
                    "      GROUP BY Publication_series.ps_id\n" +
                    "      ORDER BY number_publications_with_award\n" +
                    ")\n" +
                    "WHERE ROWNUM = 1",

            "SELECT mAwardCategory.ac_id, Authors.a_name\n" +
                    "FROM Award_categories mAwardCategory, Authors\n" +
                    "WHERE Authors.author_id IN (\n" +
                    "\n" +
                    "   SELECT Authors.author_id\n" +
                    "   FROM (\n" +
                    "     SELECT Authors.author_id, COUNT(Awards.award_id) as number_awards\n" +
                    "      FROM Authors, Awards, Title_award, Title, Publications, Publication_authors, Publication_content\n" +
                    "      WHERE Awards.award_category_id = mAwardCategory.ac_id AND\n" +
                    "            Title_award.title_id = Title.title_id AND\n" +
                    "            Title_award.award_id = Awards.award_id AND\n" +
                    "            Publication_content.publication_id = Publications.publication_id AND\n" +
                    "            Publication_content.title_id = Title.title_id AND\n" +
                    "            Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "            Publication_authors.author_id = Authors.author_id\n" +
                    "      GROUP BY Authors.author_id\n" +
                    "      ORDER BY number_awards DESC\n" +
                    "    )\n" +
                    "    WHERE ROWNUM <= 3\n" +
                    "\n" +
                    ")",

            "SELECT a_name\n" +
                    "FROM (\n" +
                    "    SELECT DISTINCT Authors.a_name, Authors.birthdate\n" +
                    "    FROM Authors, Publications, Publication_authors, Title, Publication_content\n" +
                    "    WHERE Title.title_type = 'ANTHOLOGY' AND\n" +
                    "          Publication_content.title_id = Title.title_id AND\n" +
                    "          Publications.publication_id = Publication_content.publication_id AND\n" +
                    "          Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "          Publication_authors.author_id = Authors.author_id AND\n" +
                    "          Authors.deathdate IS NULL\n" +
                    "    ORDER BY Authors.birthdate\n" +
                    ")",


            "SELECT author_id\n" +
                    "FROM (\n" +
                    "  SELECT Authors.author_id as author_id, COUNT(Title.title_id) AS number_reviews\n" +
                    "  FROM Authors, Title, Publications, Publication_content, Publication_authors\n" +
                    "  WHERE Title.title_type = 'REVIEW' AND\n" +
                    "        Publication_content.title_id = Title.title_id AND\n" +
                    "        Publication_content.publication_id = Publications.publication_id AND\n" +
                    "        Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "        Publication_authors.author_id = Authors.author_id\n" +
                    "  GROUP BY Authors.author_id\n" +
                    "  ORDER BY number_reviews DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM = 1",

            "SELECT mLanguage.language_id\n" +
                    "FROM Languages mLanguage, Authors\n" +
                    "WHERE Authors.author_id IN (\n" +
                    "  -- Top 3 authors wih the most translated titles of \"novel\" type for the given language\n" +
                    "  SELECT Authors.author_id\n" +
                    "  FROM (\n" +
                    "    SELECT Authors.author_id, COUNT(Title.title_id) number_translated_novels\n" +
                    "    FROM Authors, Title, Languages, Publication_authors, Publication_content, Publications\n" +
                    "    WHERE Publication_content.publication_id = Publications.publication_id AND\n" +
                    "          Publication_content.title_id = Title.title_id AND\n" +
                    "          Publication_authors.author_id = Authors.author_id AND\n" +
                    "          Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "          Languages.language_id = Authors.language_id AND\n" +
                    "          Languages.language_id = mLanguage.language_id AND\n" +
                    "          Title.title_translator IS NOT NULL AND\n" +
                    "          Title.title_type = 'NOVEL'\n" +
                    "    GROUP BY Authors.author_id\n" +
                    "    ORDER BY number_translated_novels DESC\n" +
                    "  )\n" +
                    "  WHERE ROWNUM <= 3\n" +
                    ")",

            "SELECT *\n" +
                    "FROM (\n" +
                    "  SELECT Authors.author_id, AVG(Publications.publication_pages / Publications.price_value) average_price_per_page\n" +
                    "  FROM Authors, Publications, Publication_authors\n" +
                    "  WHERE Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "        Publication_authors.author_id = Authors.author_id AND\n" +
                    "        Publications.PRICE_CURRENCY = '$' AND\n" +
                    "        Publications.PRICE_VALUE IS NOT NULL AND\n" +
                    "        Publications.price_value <> 0\n" +
                    "  GROUP BY Authors.author_id\n" +
                    "  ORDER BY average_price_per_page DESC\n" +
                    ")\n" +
                    "WHERE ROWNUM <= 10",


            "SELECT *\n" +
                    "FROM (\n" +
                    "  SELECT Publications.publication_id, COUNT(Web_pages.wp_id) AS web_presence\n" +
                    "  FROM Publications, Awards, Title, Web_pages, Title_award, Publication_content, Publication_authors, Title_series, Publication_series, Publisher, Authors\n" +
                    "  WHERE -- Publications with the Nebula award\n" +
                    "        Publication_content.publication_id = Publications.publication_id AND\n" +
                    "        Publication_content.title_id = Title.title_id AND\n" +
                    "        Title_award.title_id = Title.title_id AND\n" +
                    "        Title_award.award_id = Awards.award_id AND\n" +
                    "        Awards.award_title = '%Nebula%' AND (\n" +
                    "\n" +
                    "          --webpage with the author of that publication\n" +
                    "          (Publication_authors.publication_id = Publications.publication_id AND\n" +
                    "          Publication_authors.author_id = Authors.author_id AND\n" +
                    "          Web_pages.author_id = Authors.author_id) OR\n" +
                    "\n" +
                    "          --webpages with the publisher of that publication\n" +
                    "          (Publisher.publisher_id = Publications.publisher_id AND\n" +
                    "          Web_pages.publisher_id = Publisher.publisher_id) OR\n" +
                    "\n" +
                    "          --webpages with the publication series of that publication\n" +
                    "          (Publication_series.ps_id = Publicatiom_series_id AND\n" +
                    "          Web_pages.ps_id = Publication_series.ps_id) OR\n" +
                    "\n" +
                    "          --webpages with the title series of that publication\n" +
                    "          (Title_series.id = Title.series_id AND\n" +
                    "          Web_pages.ts_id = Title_series.id)\n" +
                    "\n" +
                    "        )\n" +
                    "  GROUP BY Publications.publication_id\n" +
                    "  ORDER BY web_presence DESC\n" +
                    ")"


    };


    private String[] descriptions = {
            "Number of Publications per year",
            "Ten authors with most publications",
            "Names of the youngest and oldest authors to publish something in 2010",
            "Comics with publications with less than 50 pages",
            "Comics with publications with less than 100 pages",
            "Comics with publications with more (or equal) than 100 pages",
            "Average price of Author's published novels (the ones that have a dollar price)",
            "Name of the author with the highest number of titles tagged as science fiction",
            "Three most popular titles",
            "Average price per currency of the publications of the most popular title",
            "Names of the top ten title series with most awards",
            "Name of the author who has received the most awards after his/her death",
            "Three publishers that published the most publications for a given year",
            "Most reviewed title for a given author",
            "Top three title types with most translations for every language",
            "Average number of authors per publisher for each year",
            "Publication series with most titles that have been given awards of “World Fantasy Award” type",
            "Names of the three most awarded authors for every award category",
            "Names of all living authors that have published at least one anthology from youngest to oldest.",
            "Author who has reviewed the most titles.",
            "Three authors with the most translated titles of “novel” type for every language",
            "Top ten authors  whose publications have  the largest pages per dollar ratio",
            "Top 10 publications that have been awarded the Nebula award with the most extensive web presence"

    };


    public String[] getQueries() {
        return queries;
    }

    public String[] getDescription() {
        return descriptions;
    }

}
