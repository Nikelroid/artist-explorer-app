import { Component ,OnInit,OnDestroy,NgZone} from '@angular/core';
import { DataService } from './data.service';
import { Artist,Artwork,BioGraphy,Category,FavoritObject} from './artist.model';
import { NgFor,NgStyle } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { sha256, sha224 } from 'js-sha256';
import { Subscription, interval } from 'rxjs';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';



interface Alert {
  id: number;
  message: string;
  type: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [NgFor,FormsModule, NgStyle,],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  template: `
    <input (input)="onInputChange($event)" [value]="inputValue" />
    <button (click)="fetchData()" [disabled]="isDisabled">Get Data</button>
    <div *ngIf="!isHide">{{ dataInput }}</div>
  `
})


export class AppComponent implements OnDestroy, OnInit {
  

  

  constructor(@Inject(PLATFORM_ID) private platformId: Object,
  private dataService: DataService,
  private ngZone: NgZone) {}

 logwithCookie(result : boolean){
      const artistName = localStorage.getItem('lastArtist');
      const tab = localStorage.getItem('lastTab');
      console.log('tab:',tab);
      this.tabController = 'search';
      if (tab=='search'){
        this.tabController = tab;
        this.activateSearch();
      if (artistName) {
        this.getDescription(artistName);
      }
    }else if (tab=='login'){
      this.tabController = tab;
      this.activateLogin();
    }else if (tab=='register'){
      this.tabController = tab;
      this.activateRegister();
    }else if (tab=='favorite'){
      if (result){
        this.tabController = tab;
        this.activateFavorites();
      }
    }
  }

  async initializeInterval(){
    if (!this.timeSubscription) {
    this.ngZone.runOutsideAngular(() => {
      setInterval(() => {
        this.ngZone.run(() => {
          this.startRefreshing();
        });
      }, 1000);  
    });
  }
  }
  
  tabController = '';
  ngOnInit(): void {
    if (!isPlatformBrowser(this.platformId)) return
      this.trigger();
  }

trigger(){
  this.dataService.loginWithcookie().subscribe({
    next: (data) => {
      if (data.acknowledged=='Unauthorized'){
        this.userNotFound();
        this.logwithCookie(false);
      }else{
        this.initializeInterval();
        this.logInStatus = true;
        this.userInfo.id=data.id;
        this.userInfo.name=data.name;
        this.userInfo.avatar = data.avatar;
        this.updateFavorite('','retrieve');
        this.clearLogRegForm();
        this.favoriteDic = {};
        this.userInfo.favorites.forEach(fav => {
        this.favoriteDic[fav.artistId] = true;
      });
      this.logwithCookie(true);
    }
    },
    error: (err) => {
      this.isLoading = false;
      console.error('Error:', err);
      this.logwithCookie(false);
    }
  });
  
}

currentTime: Date = new Date();
  private timeSubscription!: Subscription;

stopRefreshing() {
  if (this.timeSubscription) {
    this.timeSubscription.unsubscribe();
  }
}

startRefreshing() {
    console.log('Interval triggered and Angular zone entered for stability');
    this.timeSubscription = interval(1000).subscribe(() => {
      this.currentTime = new Date();
    });
}

ngOnDestroy() {
  if (this.timeSubscription) {
    this.timeSubscription.unsubscribe();
  }
}

  


  title = 'NimaKelidari-HW3';
  activeArtist = '';
  isDisabled = true;
  isLoading = false;
  dataInput = '';
  inputValue = '';
  isResultHidden = true;
  items: Artist[] = [];
  similars: BioGraphy[] = [];
  isLoadingBio = false;
  biographyParagraphedText = ''
  artistDesc: BioGraphy = new BioGraphy();
  isBioAvailable = false;
  artistContent = '';
  isShowArtwoks = false;
  activeClassArtWork = 'flex-sm-fill text-sm-center nav-link button-nav-app-not-focused';
  activeClassInfo = 'flex-sm-fill text-sm-center nav-link button-app';
  artWorkLink = '';
  artWorkData: Artwork[] = [];
  catResultIsReady = false;
  categories: Category[] = [];
  activeArtWork = {'year':'0','title':'','src':''}
  regInputs = {'name':'','email':'','pass':''};
  regValid = {'name':false,'email':false,'pass':false}
  baseNavBntClasses = "btn w-100 text-start text-md-center"
  navBtnClasses = {'search':this.baseNavBntClasses + " button-app",
    'login':this.baseNavBntClasses,
    'register':this.baseNavBntClasses,
    'favorite':this.baseNavBntClasses,
    'profile':this.baseNavBntClasses + ' nav-link dropdown-toggle'}
  regButtonDisabled = true;
  emailErrorText = 'Email must be valid.';
  passErrorText = 'Password is required.'
  logInStatus = false;
  userInfo = {id:'',name:'',avatar:'',favorites: []} as {
    id: string;
    name: string;
    avatar: string;
    favorites: FavoritObject[];
  };
  logButtonDisabled = true;
  isSimilarReady = false;
  isFavoriteReady = false;
  favoriteDic: { [key: string]: boolean } = {};
  splitters = [',','-']
  foundResult = {'search': false, 'artwork': false,'favorite':false,'genre':false};
 



  onInputChange(event: Event) {
    this.categories.push(new Category());
    this.inputValue = (event.target as HTMLInputElement).value;
    this.isDisabled = this.inputValue === '';
  }

  startSearching() {
    localStorage.removeItem('lastArtist');
    this.isLoading = true;
    this.dataService.searchArtist(this.inputValue.replace(/ /g, "+")).subscribe({
      next: (data) => {
        if (data.length === 0){
          this.foundResult.search = true;
        }else{
          this.foundResult.search = false;
        }
        this.isLoading = false;
        this.items = data;
        this.isResultHidden = false;
        data.forEach(element => {
          if ("/assets/shared/missing_image.png" === element._links.thumbnail.href){
            element._links.thumbnail.href = "artsy_logo.svg";
          }
        });
      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error:', err);
        this.dataInput = `Error:  + ${err.message}`;
      }
    });
  }

  getDescription(link:string){
    this.artWorkData =[];
    this.showBioArtist();
    this.isLoadingBio = true;
    this.activeArtist = link;
    this.dataService.getBio(link.split('/artists/')[1]).subscribe({
      next: (data) => {
        this.biographyParagraphedText = this.addParagraphs(data.biography);
        this.artistDesc = data;
        this.splitters = [', ','-']
        if (this.artistDesc.deathday==='' && this.artistDesc.birthday==='') {
          if (this.artistDesc.nationality==''){
            this.splitters[0] = '';
          }
          this.splitters[1] = '';
        }
        
        this.isLoadingBio = false;
        this.isBioAvailable = true;
        this.artWorkLink = data._links.artworks.href;
        this.getSimilarArtists(data._links.similar_artists.href);
        this.fetchArtworks();
        this.activeArtist = link;
        localStorage.setItem('lastArtist', link);

      },
      error: (err) => {
        this.isLoadingBio = false;
        console.error('Error:', err);
        this.dataInput = `Error:  + ${err.message}`;
      }
    });


  }

  fetchArtworks() {
    this.dataService.getArtWorks(this.artWorkLink.split('?')[1]).subscribe({
      next: (data) => {
        console.log('Artworks data:', data);
        if (data._embedded.artworks.length === 0){
          this.foundResult.artwork = true;
        }else{
          this.foundResult.artwork = false;
        }
        this.artWorkData = data._embedded.artworks 
      },
      error: (err) => {
        this.isLoadingBio = false;
        console.error('Error:', err);
        this.dataInput = `Error:  + ${err.message}`;
      }
    });
  }

  getSimilarArtists(link:string){
    this.dataService.getSimilarArtists(link.split('=')[1]).subscribe({
      next: (data) => {
        this.similars = data;
        data.forEach(element => {
          if ("/assets/shared/missing_image.png" === element._links.thumbnail.href){
            element._links.thumbnail.href = "artsy_logo.svg";
          }
        });
        if (data.length === 0) {
          this.isSimilarReady = false;
        }else{
          this.isSimilarReady = true;
        }

      },
      error: (err) => {
        this.isLoading = false;
        console.error('Error:', err);
        this.dataInput = `Error:  + ${err.message}`;
      }
    });

  }


addParagraphs(text: string): string {
  const paragraphs = text.split('\n\n');
  let result = '';

  let i = 0;
  for (const paragraph of paragraphs) {
    const lines = paragraph.split('\n');
    let newLine = lines[0];
    for (let i = 1; i < lines.length; i++) {
      newLine += `<br>${lines[i]}`
    }   
    result += `<p>${newLine}</p>`;
  }

  return result.replace(/- /g, ""); 
}

showBioArtist(){
  this.artistContent = `
  <h4 style="text-align: center; margin: 0; padding: 0;" >{{artistDesc.name}}</h4>
  <p style="text-align: center; font-size: smaller;" >{{artistDesc.nationality}}, {{artistDesc.birthday}} - {{artistDesc.deathday}}</p>
  <div style="font-size: smaller;" [innerHtml]="biographyParagraphedText"></div>
  `
}

clearScreen(){
  this.activeArtist = '';
  this.isDisabled = true;
  this.isLoading = false;
  this.dataInput = '';
  this.inputValue = '';
  this.isResultHidden = true;
  this.items = [];
  this.similars = [];
  this.isLoadingBio = false;
  this.biographyParagraphedText = ''
  this.artistDesc = new BioGraphy();
  this.isBioAvailable = false;
  this.artistContent = '';
  this.isShowArtwoks = false;
  this.activeClassArtWork = 'flex-sm-fill text-sm-center nav-link button-nav-app-not-focused';
  this.activeClassInfo = 'flex-sm-fill text-sm-center nav-link button-app';
  this.artWorkLink = '';
  this.artWorkData = [];
  this.catResultIsReady = false;
  this.categories = [];
  this.activeArtWork = {'year':'0','title':'','src':''}
  this.regInputs = {'name':'','email':'','pass':''};
  this.regValid = {'name':false,'email':false,'pass':false}
  this.isSimilarReady = false;
  this.splitters = [',','-']
  this.foundResult = {'search': false, 'artwork': false,'favorite':false,'genre':false};
  this.isFavoriteReady = false;
  localStorage.removeItem('lastArtist');

}

showArtworks(){
  this.foundResult.search = false;
  this.isShowArtwoks = true;
  this.toggleActiveTab();
}

showArtistInfo(){
  this.foundResult.search = false;
  this.isShowArtwoks = false;
  this.toggleActiveTab();
} 

toggleActiveTab(){
  if (!this.isShowArtwoks){
    this.activeClassArtWork='flex-sm-fill text-sm-center nav-link button-nav-app-not-focused';
    this.activeClassInfo='flex-sm-fill text-sm-center nav-link button-app';
  }else{
    this.activeClassArtWork='flex-sm-fill text-sm-center nav-link button-app';
    this.activeClassInfo='flex-sm-fill text-sm-center nav-link button-nav-app-not-focused';
  }
}

getCategories(id: string, title: string,year: string,src: string){
  this.activeArtWork.title=title
  this.activeArtWork.year=year
  this.activeArtWork.src=src
  this.catResultIsReady = false;
  this.dataService.getArtCategories(id).subscribe({
    next: (data) => {
      if (data._embedded.genes.length === 0){
        this.foundResult.genre = true;
      }else{
        this.foundResult.genre = false;
      }
      this.categories = data._embedded.genes;
      this.catResultIsReady = true;
    },
    error: (err) => {
      this.isLoadingBio = false;
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });
}

activateLogin(){
  this.clearLogRegForm();
  this.navBtnClasses['register']  = this.baseNavBntClasses;
  this.navBtnClasses['search']  = this.baseNavBntClasses;
  this.navBtnClasses['login'] = this.baseNavBntClasses + " button-app"
  this.navBtnClasses['favorite'] = this.baseNavBntClasses
  this.tabController = 'login';
  localStorage.setItem('lastTab', this.tabController);
}

activateRegister(){
  this.clearLogRegForm();
  this.navBtnClasses['login']  = this.baseNavBntClasses;
  this.navBtnClasses['search']  = this.baseNavBntClasses;
  this.navBtnClasses['register']= this.baseNavBntClasses + " button-app";
  this.navBtnClasses['favorite'] = this.baseNavBntClasses
  this.tabController = 'register';
  localStorage.setItem('lastTab', this.tabController);
}

activateSearch(){
  this.foundResult.search = false;
  this.foundResult.artwork = false;
  this.clearLogRegForm();
  this.navBtnClasses['login']  = this.baseNavBntClasses;
  this.navBtnClasses['register']  = this.baseNavBntClasses;
  this.navBtnClasses['search']= this.baseNavBntClasses + " button-app";
  this.navBtnClasses['favorite'] = this.baseNavBntClasses
  this.tabController = 'search';
  localStorage.removeItem('lastArtist');
  localStorage.setItem('lastTab', this.tabController);
}

activateFavorites(){
  this.foundResult.search = false;
  
  if (!this.logInStatus) {
    this.tabController = 'login';
    this.activateLogin();
    return;
  }
  this.updateFavorite('','retrieve');
  this.navBtnClasses['search'] = this.baseNavBntClasses;
  this.navBtnClasses['login'] = this.baseNavBntClasses;
  this.navBtnClasses['register'] = this.baseNavBntClasses;
  this.navBtnClasses['favorite'] = this.baseNavBntClasses + " button-app";
  this.tabController = 'favorite';
  localStorage.setItem('lastTab', this.tabController);
}

logIn(){
  const passHashed = sha256(this.regInputs.pass)
  const emailHashed = sha224(this.regInputs.email.toLowerCase())
  this.dataService.logInUser(passHashed,emailHashed).subscribe({
    next: (data) => {
      if (data.acknowledged=='notfound'){
      this.userNotFound();
    }else{
      this.initializeInterval();
      this.tabController = 'search';
      this.logInStatus = true;
      this.userInfo.id=data.id;
      this.userInfo.name=data.name;
      this.userInfo.avatar = data.avatar;
      this.updateFavorite('','retrieve');
      this.clearLogRegForm();
      this.activateSearch();
      this.favoriteDic = {};
      this.userInfo.favorites.forEach(fav => {
        this.favoriteDic[fav.artistId] = true;
    });
    }
    },
    error: (err) => {
      this.isLoadingBio = false;
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });
}

Reg(){
  const passHashed = sha256(this.regInputs.pass)
  const emailHashed = sha224(this.regInputs.email.toLowerCase())
  this.dataService.registerUser(passHashed,emailHashed,this.regInputs.name).subscribe({
    next: (data) => {
      if (data.acknowledged == 'exist'){
      this.emailArleadyExist();
    }else{
      this.initializeInterval();
      this.logInStatus = true;
      this.userInfo.id=data.insertedId;
      this.userInfo.name=this.regInputs['name'];
      this.userInfo.avatar = data.avatar;
      this.clearLogRegForm();
      this.activateSearch();
    }
    },
    error: (err) => {
      this.isLoadingBio = false;
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });
}

emailArleadyExist(){
  this.emailErrorText = 'User with this email already exists.';
  this.regValid['email'] = true;
}

userNotFound(){
  this.passErrorText = 'Password or email is incorrect.';
  this.regValid['pass'] = true;
}

validateEmail() {
  const emailPattern = /^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$/;
  this.regValid['email'] = !emailPattern.test(this.regInputs['email']);
}
validateName() {
  const namePattern = /[A-Za-z]{3,}/;
  this.regValid['name'] = !namePattern.test(this.regInputs['name']);
}
validatePassword() {
  this.regValid['pass'] = this.regInputs['pass'] === '';

}

onRegChange(mode: keyof typeof this.regValid) {
  this.regValid['pass'] = false;
  this.regValid['email'] = false;
  this.emailErrorText = 'Email must be valid.';
  this.regValid[mode] = false;
  const emailPattern = /^[A-Za-z0-9._%+\-]+@[A-Za-z0-9.\-]+\.[A-Za-z]{2,}$/;
  const namePattern = /[A-Za-z]{3,}/;
  this.regButtonDisabled = !emailPattern.test(this.regInputs['email'])||!namePattern.test(this.regInputs['name'])||this.regInputs['pass'] === '';
  this.logButtonDisabled = !emailPattern.test(this.regInputs['email'])||this.regInputs['pass'] === '';
}

clearLogRegForm(){
  this.regInputs = {email:'',name:'',pass:''}
  this.regValid = {email:false,name:false,pass:false}
}




logOut(){
  this.dataService.logOutRequest().subscribe({
    next: (data) => {
      this.userInfo = {} as {
        id: string;
        name: string;
        avatar: string;
        favorites: FavoritObject[];
      };
      this.favoriteDic = {};
      this.logInStatus = false;
      this.userInfo = {id:'',name:'',avatar:'',favorites:[]}
      this.activateSearch();
      this.stopRefreshing();
      this.showAlert("Logged out",'success')
    },
    error: (err) => {
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });
}

deleteAccount(){
  this.dataService.deleteAccount().subscribe({
    next: (data) => {
      if (data.deletedCount!==0){
        this.userInfo = {} as {
          id: string;
          name: string;
          avatar: string;
          favorites: FavoritObject[];
        };
        this.favoriteDic = {};
      this.logInStatus = false;
      this.userInfo = {id:'',name:'',avatar:'',favorites:[]}
      this.activateSearch();
      this.stopRefreshing();
      this.showAlert("Account deleted",'danger')
    }else{
      console.error('Error: cannot catch the acount');
    }
    },
    error: (err) => {
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });

}

removeFavorite(item: string,isLink: boolean = true,event:MouseEvent) {
  event.stopPropagation();
  this.toggleFavorite(item,isLink);
}

toggleFavorite(item: string,isLink: boolean = true) {
  

  var artistId = '';
  if (isLink){
  artistId = item.split('/artists/')[1];
}else{
  artistId = item;
}
  var action : string;
  if (this.favoriteDic[artistId]) {
    this.favoriteDic[artistId] = false;
    action = 'remove';
    this.showAlert("Removed from favorites",'danger')
  } else {
    this.showAlert("Added to favorites",'success')
    this.favoriteDic[artistId] = true;
    action = 'add';
  }
  
  this.updateFavorite(artistId,action);
}


updateFavorite(favId : string,action: string) {
  this.foundResult.favorite = false;
  this.isFavoriteReady = false;
  this.dataService.changeFavorite(favId,action).subscribe({
    next: (data) => {

      console.log('Favorite update response:', data);
      
      this.userInfo.favorites = Array.isArray(data) ? data : [data];
      this.userInfo.favorites.reverse();
      if (this.userInfo.favorites.length === 0){
        this.foundResult.favorite = true;
      }else{
        this.foundResult.favorite = false;
      }
      this.favoriteDic = {};
      this.userInfo.favorites.forEach(fav => {
        this.favoriteDic[fav.artistId] = true;
    });
    this.isFavoriteReady = true;

    },
    error: (err) => {
      this.isLoadingBio = false;
      console.error('Error:', err);
      this.dataInput = `Error:  + ${err.message}`;
    }
  });
}

generateClassName(link: string) {
  if (this.logInStatus){
  const artistId = link.split('/artists/')[1];
  if (this.favoriteDic[artistId]) {
    return "d-flex fa-solid fa-star align-middle";
  }else{
    return "d-flex fa-regular fa-star align-middle";
  }
}
return "d-flex fa-regular fa-star align-middle";
}

generateClassNameForColor(link: string,searchMode: boolean = true) {
  if (this.logInStatus){
  const artistId = link.split('/artists/')[1];
  if (this.favoriteDic[artistId]) {
    if (searchMode) {
      return "color : gold; ";
      }else{
        return "color : gold; font-size : 20px;";
      }
  }else{
    if (searchMode) {
    return "color : white;";
    }else{
      return "color : black; font-size : 20px;";
    }
  }
}
  return "color : white;"; 
}


getFavTime(dateAndTime:Date){
  const creatingTime = new Date(dateAndTime);
  const diff = this.currentTime.getTime() - creatingTime.getTime();
  const seconds = Math.floor(diff / 1000);
  if (seconds <= 60) {
    if (seconds === 1) {
      return `${seconds} second ago`;
    }else{
      return `${seconds} seconds ago`;
    }
  } else if (seconds <= 3600) {
    const minutes = Math.floor(seconds / 60);
    if (minutes === 1) {
      return `${minutes} minute ago`;
    }else{  
    return `${minutes} minutes ago`;
    }
  } else if (seconds <= 86400) {
    const hours = Math.floor(seconds / 3600);
    if (hours === 1) {
      return `${hours} hour ago`;
    }else{ 
    return `${hours} hours ago`;
    }
  } else {
    const days = Math.floor(seconds / 86400);
    if (days === 1) {
      return `${days} day ago`;
    }else{
    return `${days} days ago`;
    }
  }


}


 alerts: Alert[] = [];
  private alertIdCounter = 0;

  showAlert(message: string,type:string) {
    const id = this.alertIdCounter++;
    this.alerts.push({ id, message,type });
    setTimeout(() => {
      this.removeAlert(id);
    }, 3000);


  }

  removeAlert(id: number) {
    const alertToRemove = this.alerts.find(alert => alert.id === id);
    if (alertToRemove) {
      this.alerts = this.alerts.filter(alert => alert.id !== id);
    }
  }

  makeDescriptionFullScreen(){
    this.isResultHidden = true;
  }

  styleController(link : string){
    if (this.activeArtist === link){
      return 'card card-clicked';
    }else{
      return 'card';
    }
  }

  favoriteCardClick(artistId: string){
    this.activateSearch();
    this.getDescription('X/artists/'+artistId);
  }

}