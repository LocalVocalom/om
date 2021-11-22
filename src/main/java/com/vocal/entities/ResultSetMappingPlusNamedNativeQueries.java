package com.vocal.entities;

import java.util.Date;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;

import com.vocal.viewmodel.NewsDtoInnerAll;
import com.vocal.viewmodel.NewsDtoInnerUser;
import com.vocal.viewmodel.PersonalizedDto;

@MappedSuperclass
@SqlResultSetMappings({ // @formatter:off
	@SqlResultSetMapping(
			name = "newsDtoProperMappingForAll", 
			classes = { 
					@ConstructorResult(
							targetClass = NewsDtoInnerAll.class, 
							columns = {
									@ColumnResult(name = "news_id", type = Long.class),
									@ColumnResult(name = "date_time", type = Date.class),
									@ColumnResult(name = "views", type = Integer.class),
									@ColumnResult(name = "likes", type = Integer.class),
									@ColumnResult(name = "news_location"), 
									@ColumnResult(name = "news_headline"),
									@ColumnResult(name = "news_discription_text"), 
									@ColumnResult(name = "news_discription_audio_url"),
									@ColumnResult(name = "news_image_url"), 
									@ColumnResult(name = "news_video_url"),
									@ColumnResult(name = "news_creator"), 
									@ColumnResult(name = "dislikes", type = Integer.class), 
									@ColumnResult(name = "share", type = Integer.class),
									@ColumnResult(name = "comment", type = Integer.class), 
									@ColumnResult(name = "flage", type = Integer.class), 
									@ColumnResult(name = "news_url"),
									@ColumnResult(name = "openAction", type = String.class), 
									@ColumnResult(name = "aggregator", type = Boolean.class),
									@ColumnResult(name = "isReporter", type = Boolean.class),
									@ColumnResult(name = "createrId", type = Long.class)
							}
					) 
			}
	),
	@SqlResultSetMapping(
			name = "newsDtoProperMappingForUser", 
			classes = {
					@ConstructorResult(
							targetClass = NewsDtoInnerUser.class, 
							columns = {
									@ColumnResult(name = "news_id", type = Long.class),
									@ColumnResult(name = "date_time", type = Date.class),
									@ColumnResult(name = "views", type = Integer.class),
									@ColumnResult(name = "likes", type = Integer.class),
									@ColumnResult(name = "news_location"), 
									@ColumnResult(name = "news_headline"),
									@ColumnResult(name = "news_discription_text"), 
									@ColumnResult(name = "news_discription_audio_url"),
									@ColumnResult(name = "news_image_url"), 
									@ColumnResult(name = "news_video_url"),
									@ColumnResult(name = "news_creator"), 
									@ColumnResult(name = "dislikes", type = Integer.class), 
									@ColumnResult(name = "share", type = Integer.class),
									@ColumnResult(name = "comment", type = Integer.class), 
									@ColumnResult(name = "flage", type = Integer.class), 
									@ColumnResult(name = "news_url"),
									@ColumnResult(name = "status" ),
									@ColumnResult(name = "openAction", type = String.class),
									@ColumnResult(name = "aggregator", type = Boolean.class),
									@ColumnResult(name = "isReporter", type = Boolean.class),
									@ColumnResult(name = "createrId", type = Long.class)
							}
					) 
			}
	),
	@SqlResultSetMapping(
			name = "newsFeedMapping",
			classes = {
					@ConstructorResult(
							targetClass = PersonalizedDto.class,
							columns = {
									@ColumnResult(name = "news_id", type = Long.class),
									@ColumnResult(name = "date_time", type = Date.class),
									@ColumnResult(name = "views", type = Integer.class),
									@ColumnResult(name = "likes", type = Integer.class),
									@ColumnResult(name = "news_location"), 
									@ColumnResult(name = "news_headline"),
									@ColumnResult(name = "news_discription_text"), 
									@ColumnResult(name = "news_discription_audio_url"),
									@ColumnResult(name = "news_image_url"), 
									@ColumnResult(name = "news_video_url"),
									@ColumnResult(name = "news_creator"), 
									@ColumnResult(name = "dislikes", type = Integer.class), 
									@ColumnResult(name = "share", type = Integer.class),
									@ColumnResult(name = "comment", type = Integer.class), 
									@ColumnResult(name = "flage", type = Integer.class), 
									@ColumnResult(name = "news_url"),
									@ColumnResult(name = "openAction", type = String.class), 
									@ColumnResult(name = "aggregator", type = Boolean.class),
									@ColumnResult(name = "isReporter", type = Boolean.class),
									@ColumnResult(name = "createrId", type = Long.class),
									@ColumnResult(name = "catagory_id", type = Integer.class)
							}
					)
			}
	)
})
@NamedNativeQueries({
	@NamedNativeQuery(
			name = "getNewsForAllQuery",
			query = "SELECT "
					+ "A.news_id,"
					+ "A.date_time,"
					+ "A.views,"
					+ "A.likes,"
					+ "A.news_location,"
					+ "B.news_headline,"
					+ "B.news_discription_text,"
					+ "B.news_discription_audio_url,"
					+ "B.news_image_url,"
					+ "B.news_video_url,"
					+ "B.news_creator,"
					+ "B.dislikes,"
					+ "B.share,"
					+ "B.comment,"
					+ "B.flage,"
					+ "B.news_url,"
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE "
					+ "A.news_id = B.news_id AND "
					+ "A.catagory_id = :category AND "
					+ "A.language_id = :langID AND "
					+ "A.status = 'ACTIVE' AND "
					//+ "A.date_time < :datetime " 
					+ "A.news_id < :lastNewsId "//// to do better paging and faster query
					+ "ORDER BY A.date_time DESC LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForAll"
	),
	@NamedNativeQuery(
			name = "getNewsForUserQuery",
			query = "SELECT "
					+ "A.news_id,"
					+ "A.date_time,"
					+ "A.views,"
					+ "A.likes,"
					+ "A.news_location,"
					+ "B.news_headline,"
					+ "B.news_discription_text,"
					+ "B.news_discription_audio_url,"
					+ "B.news_image_url,"
					+ "B.news_video_url,"
					+ "B.news_creator,"
					+ "B.dislikes,"
					+ "B.share,"
					+ "B.comment,"
					+ "B.flage,"
					+ "B.news_url,"
					+ "A.status, "
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE "
					+ "A.news_id < :lastNewsId  AND "
					+ "A.news_id = B.news_id AND "
					+ "A.catagory_id = :category AND "
					+ "B.userid = :userid "
					//+ "A.date_time < :datetime "
					//+ "A.news_id < :lastNewsId "
					+ "ORDER BY A.date_time DESC LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForUser"
	), 
	@NamedNativeQuery(
			name = "distanceQuery", 
			query = "SELECT "
					+ "( 6371 * acos("
					+ "					cos(radians(:userLatitude)) * "
					+ "					cos(radians(latitude)) * "
					+ "					cos( radians(longitude ) - radians(:userLongitude) ) +   "
					+ "					sin(radians(:userLatitude)) * "
					+ "					sin(radians(latitude)) "
					+ "				)"
					+ ")  AS distance "
					+ "FROM NEWS WHERE news_id = :newsID"
	),
	@NamedNativeQuery(
			name = "localizedNewsQuery",
			query = "SELECT " 
					+ "A.news_id, "
					+ "A.date_time, "
					+ "A.views, "
					+ "A.likes, "
					+ "A.news_location, "
					+ "B.news_headline, "
					+ "B.news_discription_text, "
					+ "B.news_discription_audio_url, "
					+ "B.news_image_url, "
					+ "B.news_video_url, "
					+ "B.news_creator, "
					+ "B.dislikes, "
					+ "B.share, "
					+ "B.comment, "
					+ "B.flage, "
					+ "B.news_url, "
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId, "
					+ "(6371 * acos (		cos( radians(:userLatitude) ) * "
					+ "						cos( radians( latitude ) ) * "
					+ "						cos( radians( longitude ) - radians(:userLongitude)	) + "
					+ "						sin( radians(:userLatitude) ) * "
					+ "						sin( radians( latitude ) )"
					+ "				)"
					+ ") AS distance  "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE A.news_id = B.news_id AND "
					+ "A.catagory_id = :catagory AND "
					+ "A.language_id = :langID AND "
					+ "A.status='ACTIVE' "
					+ "HAVING distance > :targetedDistance "
					+ "ORDER BY distance ASC, A.date_time DESC   "
					//+ "ORDER BY  A.date_time DESC, distance ASC   "

					+ "LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForAll"
	),
	@NamedNativeQuery(
			name = "localizedNewsQueryV1", 
			query = "SELECT " 
					+ "A.news_id, "
					+ "A.date_time, "
					+ "A.views, "
					+ "A.likes, "
					+ "A.news_location, "
					+ "B.news_headline, "
					+ "B.news_discription_text, "
					+ "B.news_discription_audio_url, "
					+ "B.news_image_url, "
					+ "B.news_video_url, "
					+ "B.news_creator, "
					+ "B.dislikes, "
					+ "B.share, "
					+ "B.comment, "
					+ "B.flage, "
					+ "B.news_url, "
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId, "
					+ "(6371 * acos (		cos( radians(:userLatitude) ) * "
					+ "						cos( radians( latitude ) ) * "
					+ "						cos( radians( longitude ) - radians(:userLongitude)	) + "
					+ "						sin( radians(:userLatitude) ) * "
					+ "						sin( radians( latitude ) )"
					+ "				)"
					+ ") AS distance  "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE A.news_id = B.news_id AND "
					+ "A.catagory_id = :catagory AND "
					+ "A.language_id = :langID AND "
					+ "A.status='ACTIVE' "
					+ "HAVING distance > :targetedDistance  AND distance < :boundedDistance "
					+ "ORDER BY distance ASC, A.date_time DESC   "
					//+ "ORDER BY  A.date_time DESC, distance ASC   "
					+ "LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForAll"
	), 
	@NamedNativeQuery(
			name = "localizedNewsQueryV2",
			query = "SELECT "
					+ "A.news_id, " 
					+ "A.date_time, " 
					+ "A.views, " 
					+ "A.likes, " 
					+ "A.news_location, " 
					+ "B.news_headline, " 
					+ "B.news_discription_text, " 
					+ "B.news_discription_audio_url, " 
					+ "B.news_image_url,B.news_video_url, " 
					+ "B.news_creator, " 
					+ "B.dislikes, "
					+ "B.share, "
					+ "B.comment, "
					+ "B.flage, " 
					+ "B.news_url, " 
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId, "
					+ "FLOOR( "
					+ "(6371 * acos (		cos ( radians(:userLatitude) ) * "
					+ "						cos( radians( latitude ) ) * "
					+ "						cos( radians( longitude ) - radians(:userLongitude) ) + "
					+ "						sin( radians(:userLatitude) ) * "
					+ "						sin( radians( latitude ) ) "
					+ "				) "
					+ ") / 50 ) AS distance " 
					+ "from NEWS A, NEWS_DETAILS B " 
					+ "WHERE A.news_id=B.news_id AND " 
					+ "A.catagory_id = :catagory AND "
					+ "A.language_id = :langID AND "
					+ "A.status='ACTIVE' "
					+ "HAVING distance >= :targetedDistance  AND distance < :boundedDistance  AND "
					//+ "A.date_time < :datetime "
					+ "A.news_id < :lastNewsId "
					+ "ORDER BY distance ASC, A.date_time DESC "
					+ "LIMIT :count ",
			resultSetMapping = "newsDtoProperMappingForAll"
	),
	@NamedNativeQuery(
			name = "flooredDistanceQuery",
			query = "SELECT "
					+ "FLOOR( "
					+ "(6371 * acos (		cos ( radians(:userLatitude) )* "
					+ "						cos( radians( latitude ) ) * "
					+ "						cos( radians( longitude ) - radians(:userLongitude) ) + "
					+ "						sin( radians(:userLatitude) ) * "
					+ "						sin( radians( latitude ) ) "
					+ "				) "
					+ ") / 50 ) AS distance "
					+ "FROM NEWS WHERE news_id = :newsID "
	),
	@NamedNativeQuery(
			name = "getNewsForAllByReporter",
			query = "SELECT "
					+ "A.news_id,"
					+ "A.date_time,"
					+ "A.views,"
					+ "A.likes,"
					+ "A.news_location,"
					+ "B.news_headline,"
					+ "B.news_discription_text,"
					+ "B.news_discription_audio_url,"
					+ "B.news_image_url,"
					+ "B.news_video_url,"
					+ "B.news_creator,"
					+ "B.dislikes,"
					+ "B.share,"
					+ "B.comment,"
					+ "B.flage,"
					+ "B.news_url,"
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE "
					+ "A.news_id = B.news_id AND "
					+ "B.isReporter = :isReporter AND "
					+ "B.createrId = :creatorId AND "
					+ "A.status = 'ACTIVE' AND "
					//+ "A.date_time < :datetime "
					+ "A.news_id < :lastNewsId "
					+ "ORDER BY A.date_time DESC LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForAll"
	),
	@NamedNativeQuery(
			name = "getNewsForAllByReporterByLanguage",
			query = "SELECT "
					+ "A.news_id,"
					+ "A.date_time,"
					+ "A.views,"
					+ "A.likes,"
					+ "A.news_location,"
					+ "B.news_headline,"
					+ "B.news_discription_text,"
					+ "B.news_discription_audio_url,"
					+ "B.news_image_url,"
					+ "B.news_video_url,"
					+ "B.news_creator,"
					+ "B.dislikes,"
					+ "B.share,"
					+ "B.comment,"
					+ "B.flage,"
					+ "B.news_url,"
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE "
					+ "A.news_id = B.news_id AND "
					+ "B.isReporter = :isReporter AND "
					+ "B.createrId = :creatorId AND "
					+ "A.language_id = :langID AND "
					+ "A.status = 'ACTIVE' AND "
					//+ "A.date_time < :datetime "
					+ "A.news_id < :lastNewsId "
					+ "ORDER BY A.date_time DESC LIMIT :count",
			resultSetMapping = "newsDtoProperMappingForAll"
	),
	@NamedNativeQuery(
			name = "getNewsForAllByCategories",
			query = "SELECT "
					+ "A.news_id,"
					+ "A.date_time,"
					+ "A.views,"
					+ "A.likes,"
					+ "A.news_location, "
					+ "B.news_headline,"
					+ "B.news_discription_text,"
					+ "B.news_discription_audio_url,"
					+ "B.news_image_url,"
					+ "B.news_video_url,"
					+ "B.news_creator,"
					+ "B.dislikes,"
					+ "B.share,"
					+ "B.comment,"
					+ "B.flage,"
					+ "B.news_url,"
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId, "
					+ "A.catagory_id "
					+ "FROM NEWS A, NEWS_DETAILS B "
					+ "WHERE "
					+ "A.news_id = B.news_id AND "
					+ "A.catagory_id IN :category  AND "
					+ "A.language_id = :langID AND "
					+ "A.status = 'ACTIVE' "
					+ "ORDER BY A.date_time DESC LIMIT :count",
			resultSetMapping = "newsFeedMapping"
	),
	@NamedNativeQuery(
			name = "localizedNewsQueryV2GreaterThanDate",
			query = "SELECT "
					+ "A.news_id, " 
					+ "A.date_time, " 
					+ "A.views, " 
					+ "A.likes, " 
					+ "A.news_location, " 
					+ "B.news_headline, " 
					+ "B.news_discription_text, " 
					+ "B.news_discription_audio_url, " 
					+ "B.news_image_url,B.news_video_url, " 
					+ "B.news_creator, " 
					+ "B.dislikes, "
					+ "B.share, "
					+ "B.comment, "
					+ "B.flage, " 
					+ "B.news_url, " 
					+ "B.openAction, "
					+ "B.aggregator, "
					+ "B.isReporter, "
					+ "B.createrId, "
					+ "FLOOR( "
					+ "(6371 * acos (		cos ( radians(:userLatitude) ) * "
					+ "						cos( radians( latitude ) ) * "
					+ "						cos( radians( longitude ) - radians(:userLongitude) ) + "
					+ "						sin( radians(:userLatitude) ) * "
					+ "						sin( radians( latitude ) ) "
					+ "				) "
					+ ") / 50 ) AS distance " 
					+ "from NEWS A, NEWS_DETAILS B " 
					+ "WHERE "
					+ "A.news_id = B.news_id AND " 
					+ "A.catagory_id = :catagory AND "
					+ "A.language_id = :langID AND "
					+ "A.status='ACTIVE' "
					+ "HAVING "
					+ "distance >= :targetedDistance AND "
					+ "distance < :boundedDistance  AND "
					+ "A.date_time > :datetime AND " // to make sure that the news creation time are greater than the date
					+ "A.news_id < :lastNewsId "
					+ "ORDER BY distance ASC, A.date_time DESC "
					+ "LIMIT :count ",
			resultSetMapping = "newsDtoProperMappingForAll"
	)
})



// @formatter:on
public abstract class ResultSetMappingPlusNamedNativeQueries {

}
