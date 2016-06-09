-- query a: Compute the average price per currency of the publications of the most popular title (i.e, the title with
-- most publications overall).
-- tested with sql developer
SELECT mPublications.price_currency, AVG(mPublications.price_value) as average_price
FROM Publications mPublications, Title, Publication_content
WHERE Publication_content.title_id = Title.title_id AND
      Publication_content.publication_id = mPublications.publication_id AND
      Title.title_id IN (

                -- Top title and its number of publications
                SELECT title_id
                FROM (
                  SELECT Title.title_id, COUNT(Publications.publication_id) AS number_publications
                  FROM Title, Publications, Publication_content
                  WHERE Publication_content.title_id = Title.title_id AND
                        Publication_content.publication_id = Publications.publication_id
                  GROUP BY Title.title_id
                  ORDER BY number_publications DESC )
                WHERE ROWNUM = 1

)
GROUP BY mPublications.price_currency;


-- query b: Output the names of the top ten title series with most awards
-- tested with sql developer
SELECT *
FROM (
  SELECT Title_series.title, COUNT(Title_award.AWARD_ID) AS number_awards
  FROM Title_series, Title_award, Title
  WHERE Title.series_id = Title_series.id AND
        Title_award.title_id = Title.title_id
  GROUP BY Title_series.title
  ORDER BY number_awards DESC
)
WHERE ROWNUM <= 10;

-- query c: Output the name of the author who has received the most awards after his/her death.
-- tested with sql developer
SELECT *
FROM (
SELECT Authors.a_name, COUNT(Awards.award_id) AS number_awards
FROM Authors, Awards, Title_award, Title, Publication_content, Publications, Publication_authors
WHERE Publication_content.publication_id = Publications.publication_id AND
      Publication_content.title_id = Title.title_id AND
      Publication_authors.author_id = Authors.author_id AND
      Publication_authors.publication_id = Publications.publication_id AND
      Title_award.title_id = Title.title_id AND
      Awards.AWARD_DATE >= Authors.DEATHDATE
GROUP BY Authors.a_name
ORDER BY number_awards DESC
)
WHERE ROWNUM = 1;

-- query d: For a given year, output the three publishers that published the most publications.
-- Note: instead of 2000, put the desired year
-- tested with sql developer
SELECT *
FROM (
  SELECT Publisher.PUBLISHER_ID, COUNT(Publications.publication_id) as number_publications
  FROM Publications, Publisher
  WHERE EXTRACT ( YEAR FROM Publications.publication_date ) = 2000 AND
        Publications.publisher_id = Publisher.publisher_id
  GROUP BY Publisher.publisher_id
  ORDER BY number_publications DESC
)
WHERE ROWNUM <= 3;


-- query e: Given an author, compute his/her most reviewed title(s).
-- tested with sql developer
-- note: replace the hardcoded author name with the desired one
SELECT *
FROM (
SELECT Title.title, COUNT(Reviews.title_id) as number_reviews
FROM Title, Reviews, Publications, Publication_authors, Authors, Publication_content
WHERE Authors.a_name = 'H. G. Wells' AND
      Reviews.title_id = Title.title_id AND
      Publication_authors.publication_id = Publications.publication_id AND
      Publication_authors.AUTHOR_ID = Authors.author_id AND
      Publication_content.title_id = Title.TITLE_ID AND
      Publication_content.publication_id = Publications.PUBLICATION_ID
GROUP BY Title.title
ORDER BY number_reviews DESC
) WHERE ROWNUM = 2;

-- query f) For every language, find the top three title types with most translations.
-- note: no synthax errors but never returns

SELECT mLanguage.language_id, Title.title_type
FROM Title, Languages mLanguage
WHERE Title.title_type IN (

  SELECT Title.title_type
  FROM (
    -- Title types and their number of translations
    SELECT Title.title_type, COUNT(Title.title_type) number_translations
    FROM Title
    WHERE Title.title_translator IS NOT NULL AND
          Title.language_id = mLanguage.language_id
    GROUP BY Title.title_type
    ORDER BY number_translations DESC
  )
  WHERE ROWNUM <= 3

);


-- query g) For each year, compute the average number of authors per publisher.
-- tested with sql developer
-- note: the average is not always an integer. It might be good to round the result in
-- the interface
-- note 2: the result is sorted just to make it more practical for the user
SELECT EXTRACT ( YEAR FROM Publications.publication_date ), Publisher.publisher_id, AVG(Authors.author_id)
FROM Publisher, Authors, Publications, Publication_authors
WHERE Publications.PUBLICATION_ID = Publication_authors.publication_id AND
      Publication_authors.author_id = Authors.author_id AND
      Publications.publisher_id = Publisher.PUBLISHER_ID
GROUP BY Publications.publication_date, Publisher.PUBLISHER_ID
ORDER BY Publications.publication_date;

-- query h) Find the publication series with most titles that have been given awards of “World Fantasy Award” type.
-- tested with sql developer but no rows returned because most of awards has been deleted
SELECT *
FROM(
      SELECT Publication_series.ps_id, COUNT(Publications.publication_id) as number_publications_with_award
      FROM Publication_series, Publications, Awards, Title, Title_award, Publication_content
      WHERE Publications.PUBLICATIOM_SERIES_ID = Publication_series.PS_ID AND
            Publications.publication_id = Publication_content.publication_id AND
            Title.title_id = Publication_content.title_id AND
            Title_award.title_id = Title.title_id AND
            Awards.AWARD_TITLE LIKE '%World Fantasy Award%' AND
            Awards.AWARD_ID = Title_award.AWARD_ID
      GROUP BY Publication_series.ps_id
      ORDER BY number_publications_with_award
)
WHERE ROWNUM = 1;

-- query i) For every award category, list the names of the three most awarded authors
-- note: the result will look like this:
-- language1 author1
-- language1 author2
-- language1 author3
-- language2 author4
-- ...
-- tested with sql developers
SELECT mAwardCategory.ac_id, Authors.a_name
FROM Award_categories mAwardCategory, Authors
WHERE Authors.author_id IN (

   -- 3 most awarded authors for the given category
   SELECT Authors.author_id
   FROM (
     -- Number of awards per author for the given category
     SELECT Authors.author_id, COUNT(Awards.award_id) as number_awards
      FROM Authors, Awards, Title_award, Title, Publications, Publication_authors, Publication_content
      WHERE Awards.award_category_id = mAwardCategory.ac_id AND
            Title_award.title_id = Title.title_id AND
            Title_award.award_id = Awards.award_id AND
            Publication_content.publication_id = Publications.publication_id AND
            Publication_content.title_id = Title.title_id AND
            Publication_authors.publication_id = Publications.publication_id AND
            Publication_authors.author_id = Authors.author_id
      GROUP BY Authors.author_id
      ORDER BY number_awards DESC
    )
    WHERE ROWNUM <= 3

);


-- query j)Output the names of all living authors that have published at least one
-- anthology from youngest to oldest.
-- tested with sql developer
-- note: The inner query needs to select the birthdate too, otherwise the order
-- by can not be performed correctly because of the DISTINCT( there will be a problem with several
-- authors having the same name, but different birthdates ).
-- changed Authors.a_name to a_name because sql developer gave error otherwise
SELECT a_name
FROM (
    SELECT DISTINCT Authors.a_name, Authors.birthdate
    FROM Authors, Publications, Publication_authors, Title, Publication_content
    WHERE Title.title_type = 'ANTHOLOGY' AND
          Publication_content.title_id = Title.title_id AND
          Publications.publication_id = Publication_content.publication_id AND
          Publication_authors.publication_id = Publications.publication_id AND
          Publication_authors.author_id = Authors.author_id AND
          Authors.deathdate IS NULL
    ORDER BY Authors.birthdate
);

-- query l) Find the author who has reviewed the most titles.
-- tested with sql developer
SELECT author_id
FROM (
  SELECT Authors.author_id as author_id, COUNT(Title.title_id) AS number_reviews
  FROM Authors, Title, Publications, Publication_content, Publication_authors
  WHERE Title.title_type = 'REVIEW' AND
        Publication_content.title_id = Title.title_id AND
        Publication_content.publication_id = Publications.publication_id AND
        Publication_authors.publication_id = Publications.publication_id AND
        Publication_authors.author_id = Authors.author_id
  GROUP BY Authors.author_id
  ORDER BY number_reviews DESC
)
WHERE ROWNUM = 1;

-- query m)For every language, list the three authors with the most translated titles of “novel” type
-- no synthax errors but does not return
SELECT mLanguage.language_id
FROM Languages mLanguage, Authors
WHERE Authors.author_id IN (
  -- Top 3 authors wih the most translated titles of "novel" type for the given language
  SELECT Authors.author_id
  FROM (
    SELECT Authors.author_id, COUNT(Title.title_id) number_translated_novels
    FROM Authors, Title, Languages, Publication_authors, Publication_content, Publications
    WHERE Publication_content.publication_id = Publications.publication_id AND
          Publication_content.title_id = Title.title_id AND
          Publication_authors.author_id = Authors.author_id AND
          Publication_authors.publication_id = Publications.publication_id AND
          Languages.language_id = Authors.language_id AND
          Languages.language_id = mLanguage.language_id AND
          Title.title_translator IS NOT NULL AND
          Title.title_type = 'NOVEL'
    GROUP BY Authors.author_id
    ORDER BY number_translated_novels DESC
  )
  WHERE ROWNUM <= 3
);

-- query n) Order the top ten authors  whose publications have  the largest pages per dollar
-- ratio(considering all publications of an author that have a dollar price).
-- Does not work yet because need to convert publication pages into normal numbers
SELECT *
FROM (
  SELECT Authors.author_id, AVG(Publications.publication_pages / Publications.price_value) average_price_per_page
  FROM Authors, Publications, Publication_authors
  WHERE Publication_authors.publication_id = Publications.publication_id AND
        Publication_authors.author_id = Authors.author_id AND
        Publications.PRICE_CURRENCY = '$' AND
        Publications.PRICE_VALUE IS NOT NULL AND
        Publications.price_value <> 0
  GROUP BY Authors.author_id
  ORDER BY average_price_per_page DESC
)
WHERE ROWNUM <= 10;

-- query o) For publications that have been awarded the Nebula award, find the top 10 with the most extensive
-- web presence (i.e, the highest number of author websites, publication websites, publisher websites,
-- publication series websites, and title series websites in total)
-- Note: no synthax errors but never returns
SELECT *
FROM (
  SELECT Publications.publication_id, COUNT(Web_pages.wp_id) AS web_presence
  FROM Publications, Awards, Title, Web_pages, Title_award, Publication_content, Publication_authors, Title_series, Publication_series, Publisher, Authors
  WHERE -- Publications with the Nebula award
        Publication_content.publication_id = Publications.publication_id AND
        Publication_content.title_id = Title.title_id AND
        Title_award.title_id = Title.title_id AND
        Title_award.award_id = Awards.award_id AND
        Awards.award_title = '%Nebula%' AND (

          --webpage with the author of that publication
          (Publication_authors.publication_id = Publications.publication_id AND
          Publication_authors.author_id = Authors.author_id AND
          Web_pages.author_id = Authors.author_id) OR

          --webpages with the publisher of that publication
          (Publisher.publisher_id = Publications.publisher_id AND
          Web_pages.publisher_id = Publisher.publisher_id) OR

          --webpages with the publication series of that publication
          (Publication_series.ps_id = Publicatiom_series_id AND
          Web_pages.ps_id = Publication_series.ps_id) OR

          --webpages with the title series of that publication
          (Title_series.id = Title.series_id AND
          Web_pages.ts_id = Title_series.id)

        )
  GROUP BY Publications.publication_id
  ORDER BY web_presence DESC
)
WHERE ROWNUM <= 10;
 @tuvior

tuvior commented 21 hours ago
Query #1

SELECT mPublications.price_currency, AVG(mPublications.price_value) as average_price
FROM Publications mPublications, Title, Publication_content
WHERE Publication_content.title_id = Title.title_id AND
      Publication_content.publication_id = mPublications.publication_id AND
      Title.title_id IN (

                -- Top title and its number of publications
                SELECT title_id
                FROM (
                  SELECT Title.title_id, COUNT(Publications.publication_id) AS number_publications
                  FROM Title, Publications, Publication_content
                  WHERE Publication_content.title_id = Title.title_id AND
                        Publication_content.publication_id = Publications.publication_id
                  GROUP BY Title.title_id
                  ORDER BY number_publications DESC )
                WHERE ROWNUM = 1

)
GROUP BY mPublications.price_currency;
