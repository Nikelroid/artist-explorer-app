export interface Artist {
    type: string;
    title: string;
    description: string;
    og_link: string;
    _links: {
      self: {
        href:string;
      };
      permalink:{
        href: string;
        };
      thumbnail: {
        href: string;
      }
    };
  }

  export class BioGraphy {
    id: string;
    slug: string;
    created_at: string;
    updated_at: string;
    name: string;
    sortable_name: string;
    gender: string;
    biography: string;
    birthday: string;
    deathday: string;
    hometown: string;
    location: string;
    nationality: string;
    target_supply: boolean;
    image_versions: string[];
    _links: {
      thumbnail: {
        href: string;
      };
      image: {
        href: string;
        templated: boolean;
      };
      self: {
        href: string;
      };
      permalink: {
        href: string;
      };
      artworks: {
        href: string;
      };
      published_artworks: {
        href: string;
      };
      similar_artists: {
        href: string;
      };
      similar_contemporary_artists: {
        href: string;
      };
      genes: {
        href: string;
      }
    };
    constructor (data: Partial<BioGraphy> = {}) {
      this.id = data.id ?? '';
      this.slug = data.slug ?? '';
      this.created_at = data.created_at ?? '';
      this.updated_at = data.updated_at ?? '';
      this.name = data.name ?? '';
      this.sortable_name = data.sortable_name ?? '';
      this.gender = data.gender ?? '';
      this.biography = data.biography ?? '';
      this.birthday = data.birthday ?? '';
      this.deathday = data.deathday ?? '';
      this.hometown = data.hometown ?? '';
      this.location = data.location ?? '';
      this.nationality = data.nationality ?? '';
      this.target_supply = data.target_supply ?? false;
      this.image_versions = data.image_versions ?? [];
      this._links = data._links ?? {
        thumbnail: { href: '' },
        image: { href: '', templated: false },
        self: { href: '' },
        permalink: { href: '' },
        artworks: { href: '' },
        published_artworks: { href: '' },
        similar_artists: { href: '' },
        similar_contemporary_artists: { href: '' },
        genes: { href: '' }
      };
    }
  }

  export interface Artwork {
    id: string;
    slug: string;
    createdAt: string;
    updatedAt: string;
    title: string;
    category: string;
    medium: string;
    date: string;
    dimensions: {
      in: {
        text: string;
        height: number;
        width: number;
        depth?: number | null;
        diameter?: number | null;
      };
      cm: {
        text: string;
        height: number;
        width: number;
        depth?: number | null;
        diameter?: number | null;
      };
    };
    published: boolean;
    website: string;
    signature: string;
    series: string;
    provenance: string;
    literature: string;
    exhibitionHistory: string;
    collectingInstitution: string;
    additionalInformation: string;
    imageRights: string;
    blurb: string;
    unique: boolean;
    culturalMaker?: string | null;
    iconicity: number;
    canInquire: boolean;
    canAcquire: boolean;
    canShare: boolean;
    saleMessage?: string | null;
    sold: boolean;
    visibilityLevel: string;
    imageVersions: string[];
    _links: {
      thumbnail: { href: string };
      image: { href: string; templated: boolean };
      partner: { href: string };
      self: { href: string };
      permalink: { href: string };
      genes: { href: string };
      artists: { href: string };
      similarArtworks: { href: string };
      collectionUsers: { href: string };
      saleArtworks: { href: string };
    };
    embedded: {
      editions: any[]; 
    };
  }

  export class Category {
    id: string | null = null;
    createdAt: Date | null = null;
    updatedAt: Date | null = null;
    name: string | null = null;
    displayName: string | null = null;
    description: string | null = null;
    imageVersions: string[] | null = null;
    _links: {
      thumbnail: { href: string | null };
      image: { href: string | null; templated?: boolean };
      self: { href: string | null };
      permalink: { href: string | null };
      artworks: { href: string | null };
      publishedArtworks: { href: string | null };
      artists: { href: string | null };
    } = {
      thumbnail: { href: null },
      image: { href: null },
      self: { href: null },
      permalink: { href: null },
      artworks: { href: null },
      publishedArtworks: { href: null },
      artists: { href: null },
    };
  
    constructor() {
    }
  }
  export interface FavoritObject {
    artistId: string;
    title: string;
    dates: string;
    thumbnail: string;
    nationality : string;
    time: Date;
  }