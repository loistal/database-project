-- query a) For every year, output the year and the number of publications for said year.
-- tested with sql developer
SELECT EXTRACT (YEAR FROM Publications.publication_date), COUNT(Publications.publication_date)
FROM Publications
GROUP BY EXTRACT (YEAR FROM Publications.publication_date);

-- query b: Output the names of the ten authors with most publications
-- tested with sql developer
SELECT authors_number_publications.a_name
FROM (
    SELECT Authors.author_id, Authors.a_name, COUNT(*) as pubs
    FROM Authors, Publications, Publication_authors
    WHERE Publication_authors.author_id = Authors.author_id AND
          Publication_authors.publication_id = Publications.publication_id
    GROUP BY Authors.author_id, Authors.a_name
    ORDER BY pubs DESC
) authors_number_publications
WHERE ROWNUM <= 10;

-- query c What are the names of the youngest and oldest authors to publish something in 2010?
-- changes made:
-- Instead of considering only the year, the day and the month are also included.
-- Note: there are more than 2 names because these authors have the same birthdate
-- tested with sql developer
SELECT Authors.a_name
FROM Authors
WHERE Authors.birthdate IN (
        SELECT MAX(Authors.birthdate)
        FROM Authors, Publications, Publication_authors
        WHERE Publication_authors.author_id = Authors.author_id AND
        Publication_authors.publication_id = Publications.publication_id AND
        EXTRACT (YEAR FROM Publications.publication_date) = 2010

    ) OR
    Authors.birthdate IN (
      SELECT MIN(Authors.birthdate)
        FROM Authors, Publications, Publication_authors
        WHERE Publication_authors.author_id = Authors.author_id AND
        Publication_authors.publication_id = Publications.publication_id AND
        EXTRACT (YEAR FROM Publications.publication_date) = 2010
);

-- query d: How many comics (graphic titles) have publications with less than 50 pages, less than 100 pages, and more (or equal) than 100 pages?
-- tested with sql developer

    -- less than fifty pages
    SELECT COUNT ( Publications.publication_id )
    FROM Title, Publication_content, Publications
    WHERE Title.title_graphic = 'YES' AND
    Title.title_id = Publication_content.title_id AND
    Publications.publication_id = Publication_content.publication_id AND
    Publications.publication_pages < 50;

     -- less than one hundred pages
    SELECT COUNT ( Publications.publication_id )
    FROM Title, Publication_content, Publications
    WHERE Title.title_graphic = 'YES' AND
    Title.title_id = Publication_content.title_id AND
    Publications.publication_id = Publication_content.publication_id AND
    Publications.publication_pages < 100;

     -- more or equal to one hundred pages
    SELECT COUNT ( Publications.publication_id )
    FROM Title, Publication_content, Publications
    WHERE Title.title_graphic = 'YES' AND
    Title.title_id = Publication_content.title_id AND
    Publications.publication_id = Publication_content.publication_id AND
    Publications.publication_pages >= 100;


    -- query e: For every publisher, calculate the average price of its published novels (the ones that have a dollar price).
    -- A group by has been added at the end so that we get the individual average for each publisher
    -- tested with sql developer
    SELECT Publisher.publisher_id , AVG(Publications.price_value)
    FROM Publications, Publisher
    WHERE Publisher.publisher_id = Publications.publisher_id AND
          Publications.price_currency = '$'
    GROUP BY Publisher.publisher_id;

-- query f: What is the name of the author with the highest number of titles that are tagged as “science fiction”?
-- tested with sql developer
-- more flexibility with the science fiction tag by adding %
SELECT author_name
FROM (
    SELECT Authors.author_id, Authors.a_name AS author_name, COUNT ( Title.title_id ) as science_fiction_per_author
    FROM Title, Authors, Title_tags, Tags, Publication_authors, Publications, Publication_content
    WHERE Title.title_id = Title_tags.title_id AND
          Title_tags.tag_id = Tags.tag_id AND
          Tags.tag_name LIKE '%science fiction%' AND
          Publication_authors.publication_id = Publications.publication_id AND
          Authors.author_id = Publication_authors.author_id AND
          Publications.publication_id = Publication_content.publication_id AND
          Title.title_id = Publication_content.title_id
    GROUP BY Authors.author_id, Authors.a_name
    ORDER BY science_fiction_per_author DESC
)
WHERE ROWNUM = 1;

-- query g: List the three most popular titles (i.e., the ones with the most awards and reviews).
-- tested with sql developer
SELECT num_awards.title_id , num_awards + num_reviews
FROM
  (
    SELECT Title.title_id, COUNT(Title_award.award_id) as num_awards
    FROM Title, Title_award
    WHERE Title.title_id = Title_award.title_id
    GROUP BY Title.title_id
  ) num_awards,
  (
    SELECT Title.title_id , COUNT (Reviews.title_id) as num_reviews
    FROM Title, Reviews
    WHERE Title.title_id = Reviews.title_id
    GROUP BY Title.title_id
  ) num_reviews
WHERE num_awards.title_id = num_reviews.title_id;
