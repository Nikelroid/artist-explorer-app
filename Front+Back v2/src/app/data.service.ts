import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Artist, BioGraphy , Artwork,FavoritObject } from './artist.model';

@Injectable({
  providedIn: 'root'
})
export class DataService {
  constructor(private http: HttpClient) {}

  searchArtist(artistName: string) {
    return this.http.get<Artist[]>(`/api/artist/search/${artistName}`);
  }

  getBio(artistLink: string){
    return this.http.get<BioGraphy>(`/api/artist/bio/${artistLink}`);
  }

  getArtWorks(artistLink: string){
    return this.http.get<any>(`/api/artist/artwork/${artistLink}`);
  }

  getArtCategories(artId: string){
    return this.http.get<any>(`/api/artwork/categories/${artId}`);
  }

  registerUser(passHashed:string, emailHashed:string,fullname:string){
    return this.http.post<any>("/api/user/register",{pass:passHashed,email:emailHashed,name:fullname},{ withCredentials: true });
  }
  
  logInUser(passHashed:string, emailHashed:string){
    return this.http.post<any>("/api/user/login",{pass:passHashed,email:emailHashed},{ withCredentials: true });
  }

  deleteAccount(){
    return this.http.post<any>("/api/user/delete",{},{ withCredentials: true });
  }

  logOutRequest(){
    return this.http.post<any>("/api/user/logout",{},{ withCredentials: true });
  }

  getSimilarArtists(artistId:string){
    return this.http.get<BioGraphy[]>(`/api/artist/similar/${artistId}`);
  }

  changeFavorite(favoritesId:string,action: string) {
    return this.http.post<FavoritObject>("/api/user/favorites",{id:favoritesId,action: action},{ withCredentials: true });
  }

  loginWithcookie(){
    console.log("loginWithcookie");
    return this.http.get<any>("/api/me",{ withCredentials: true });
  }
}