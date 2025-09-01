package com.nikelroid.artist_pedia

import java.io.Serializable

data class ArtistBio(
    val id: String,
    val slug: String,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val sortable_name: String,
    val gender: String,
    val biography: String,
    val birthday: String,
    val deathday: String,
    val hometown: String,
    val location: String,
    val nationality: String,
    val target_supply: Boolean,
    val image_versions: List<String>,
    val _links: LinksBio
)


data class LinksBio(
    val thumbnail: Link,
    val image: TemplatedLink,
    val self: Link,
    val permalink: Link,
    val artworks: Link,
    val published_artworks: Link,
    val similar_artists: Link,
    val similar_contemporary_artists: Link,
    val genes: Link
)


data class ArtistSearchResult(
    val type: String,
    val title: String,
    val description: String?,
    val og_type: String,
    val _links: Links
)


data class Links(
    val self: Link,
    val permalink: Link,
    val thumbnail: Link
)

data class Link(
    val href: String
)



data class ArtworksResponse(
    val total_count: Int?,
    val _links: Links,
    val _embedded: EmbeddedArtworks
)

data class EmbeddedArtworks(
    val artworks: List<Artwork>
)

data class Artwork(
    val id: String,
    val slug: String,
    val created_at: String,
    val updated_at: String,
    val title: String,
    val category: String?,
    val medium: String?,
    val date: String?,
    val dimensions: Dimensions,
    val published: Boolean,
    val website: String?,
    val signature: String?,
    val series: String?,
    val provenance: String?,
    val literature: String?,
    val exhibition_history: String?,
    val collecting_institution: String?,
    val additional_information: String?,
    val image_rights: String?,
    val blurb: String?,
    val unique: Boolean,
    val cultural_maker: String?,
    val iconicity: Double,
    val can_inquire: Boolean,
    val can_acquire: Boolean,
    val can_share: Boolean,
    val sale_message: String?,
    val sold: Boolean,
    val visibility_level: String,
    val image_versions: List<String>,
    val _links: ArtworkLinks,
    val _embedded: ArtworkEmbedded
)

data class Dimensions(
    val `in`: DimensionUnit,
    val cm: DimensionUnit
)

data class DimensionUnit(
    val text: String?,
    val height: Double?,
    val width: Double?,
    val depth: Double?,
    val diameter: Double?
)

data class ArtworkLinks(
    val thumbnail: Link,
    val image: TemplatedLink,
    val partner: Link,
    val self: Link,
    val permalink: Link,
    val genes: Link,
    val artists: Link,
    val similar_artworks: Link,
    val collection_users: Link,
    val sale_artworks: Link
)

data class TemplatedLink(
    val href: String,
    val templated: Boolean
)

data class ArtworkEmbedded(
    val editions: List<Any>
)

data class GeneResponse(
    val total_count: Int?,
    val _links: PaginationLinks,
    val _embedded: EmbeddedGenes
)

data class PaginationLinks(
    val self: Link,
    val next: Link?
)


data class EmbeddedGenes(
    val genes: List<Gene>
)

data class Gene(
    val id: String,
    val created_at: String,
    val updated_at: String,
    val name: String,
    val display_name: String?,
    val description: String,
    val image_versions: List<String>,
    val _links: GeneLinks
)

data class GeneLinks(
    val thumbnail: Link,
    val image: ImageLink,
    val self: Link,
    val permalink: Link,
    val artworks: Link,
    val published_artworks: Link,
    val artists: Link
)

data class ImageLink(
    val href: String,
    val templated: Boolean
)

fun ArtistBioMaker(): ArtistBio {
    return ArtistBio(
        id = "",
        slug = "",
        created_at = "",
        updated_at = "",
        name = "",
        sortable_name = "",
        gender = "",
        biography = "",
        birthday = "",
        deathday = "",
        hometown = "",
        location = "",
        nationality = "",
        target_supply = false,
        image_versions = emptyList(),
        _links = LinksBio(
            thumbnail = Link(""),
            image = TemplatedLink("", false),
            self = Link(""),
            permalink = Link(""),
            artworks = Link(""),
            published_artworks = Link(""),
            similar_artists = Link(""),
            similar_contemporary_artists = Link(""),
            genes = Link("")
        )
    )
}



data class LoginRespond(
    var acknowledged: String,
    val id: String,
    val name: String,
    val avatar: String,
    val favorites: List<Favorite>
): Serializable


data class Favorite(
    val artistId: String,
    val time: String,
    val title: String,
    val thumbnail: String,
    val nationality: String,
    val dates: String
): Serializable
