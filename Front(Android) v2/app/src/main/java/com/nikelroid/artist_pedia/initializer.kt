package com.nikelroid.artist_pedia

fun initArtistBio(): ArtistBio {
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
        _links = initLinksBio()
    )
}

fun initLinksBio(): LinksBio {
    return LinksBio(
        thumbnail = initLink(),
        image = initTemplatedLink(),
        self = initLink(),
        permalink = initLink(),
        artworks = initLink(),
        published_artworks = initLink(),
        similar_artists = initLink(),
        similar_contemporary_artists = initLink(),
        genes = initLink()
    )
}

fun initArtistSearchResult(): ArtistSearchResult {
    return ArtistSearchResult(
        type = "",
        title = "",
        description = "",
        og_type = "",
        _links = initLinks()
    )
}

fun initLinks(): Links {
    return Links(
        self = initLink(),
        permalink = initLink(),
        thumbnail = initLink()
    )
}

fun initLink(): Link {
    return Link(
        href = ""
    )
}

fun initArtworksResponse(): ArtworksResponse {
    return ArtworksResponse(
        total_count = 0,
        _links = initLinks(),
        _embedded = initEmbeddedArtworks()
    )
}

fun initEmbeddedArtworks(): EmbeddedArtworks {
    return EmbeddedArtworks(
        artworks = emptyList()
    )
}

fun initArtwork(): Artwork {
    return Artwork(
        id = "",
        slug = "",
        created_at = "",
        updated_at = "",
        title = "",
        category = "",
        medium = "",
        date = "",
        dimensions = initDimensions(),
        published = false,
        website = "",
        signature = "",
        series = "",
        provenance = "",
        literature = "",
        exhibition_history = "",
        collecting_institution = "",
        additional_information = "",
        image_rights = "",
        blurb = "",
        unique = false,
        cultural_maker = "",
        iconicity = 0.0,
        can_inquire = false,
        can_acquire = false,
        can_share = false,
        sale_message = "",
        sold = false,
        visibility_level = "",
        image_versions = emptyList(),
        _links = initArtworkLinks(),
        _embedded = initArtworkEmbedded()
    )
}

fun initDimensions(): Dimensions {
    return Dimensions(
        `in` = initDimensionUnit(),
        cm = initDimensionUnit()
    )
}

fun initDimensionUnit(): DimensionUnit {
    return DimensionUnit(
        text = "",
        height = 0.0,
        width = 0.0,
        depth = 0.0,
        diameter = 0.0
    )
}

fun initArtworkLinks(): ArtworkLinks {
    return ArtworkLinks(
        thumbnail = initLink(),
        image = initTemplatedLink(),
        partner = initLink(),
        self = initLink(),
        permalink = initLink(),
        genes = initLink(),
        artists = initLink(),
        similar_artworks = initLink(),
        collection_users = initLink(),
        sale_artworks = initLink()
    )
}

fun initTemplatedLink(): TemplatedLink {
    return TemplatedLink(
        href = "",
        templated = false
    )
}

fun initArtworkEmbedded(): ArtworkEmbedded {
    return ArtworkEmbedded(
        editions = emptyList()
    )
}

fun initGeneResponse(): GeneResponse {
    return GeneResponse(
        total_count = 0,
        _links = initPaginationLinks(),
        _embedded = initEmbeddedGenes()
    )
}

fun initPaginationLinks(): PaginationLinks {
    return PaginationLinks(
        self = initLink(),
        next = initLink()
    )
}

fun initEmbeddedGenes(): EmbeddedGenes {
    return EmbeddedGenes(
        genes = emptyList()
    )
}

fun initGene(): Gene {
    return Gene(
        id = "",
        created_at = "",
        updated_at = "",
        name = "",
        display_name = "",
        description = "",
        image_versions = emptyList(),
        _links = initGeneLinks()
    )
}

fun initGeneLinks(): GeneLinks {
    return GeneLinks(
        thumbnail = initLink(),
        image = initImageLink(),
        self = initLink(),
        permalink = initLink(),
        artworks = initLink(),
        published_artworks = initLink(),
        artists = initLink()
    )
}

fun initImageLink(): ImageLink {
    return ImageLink(
        href = "",
        templated = false
    )
}

fun initLoginRespond(): LoginRespond {
    return LoginRespond(
        acknowledged = "",
        id = "",
        name = "",
        avatar = "",
        favorites = emptyList()
    )
}

fun initFavorite(): Favorite {
    return Favorite(
        artistId = "",
        time = "",
        title = "",
        thumbnail = "",
        nationality = "",
        dates = ""
    )
}