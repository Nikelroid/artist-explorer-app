import {
  AngularNodeAppEngine,
  writeResponseToNodeResponse,
  isMainModule,
  createNodeRequestHandler
} from '@angular/ssr/node';
import axios from 'axios';
import express, { Request, Response } from 'express';
import { dirname, resolve } from 'node:path';
import { fileURLToPath } from 'node:url';

const jwt = require('jsonwebtoken');
const secretKey = 'peter3445';




const { MongoClient } = require('mongodb');
const uri = "mongodb+srv://peter1343:AQsFKgTcwxItBuxn@artistpedia.wy5b2dj.mongodb.net/?retryWrites=true&w=majority&appName=ArtistPedia";
const crypto = require('crypto');

 
function getGravatarUrl(email:string, size = 80) {
    const trimmedEmail = email.trim().toLowerCase();
    const hash = crypto.createHash('sha256').update(trimmedEmail).digest('hex');
    return `https://www.gravatar.com/avatar/${hash}?s=${size}&d=identicon`;
}

const bcrypt = require('bcryptjs');
const saltRounds = 11;


const serverDistFolder = dirname(fileURLToPath(import.meta.url));
const browserDistFolder = resolve(serverDistFolder, '../browser');
let artsyToken: string | null = null;
process.env['ARTSY_CLIENT_ID'] = "b339a5de0079bd62d341";
process.env['ARTSY_CLIENT_SECRET'] = "3b526befbc64ff0a3da7e79d01de33d8";

const cookieParser = require('cookie-parser');
const app = express();
const angularApp = new AngularNodeAppEngine();

app.use(express.static('public'));
app.use('/images', express.static('images'));

app.use(cookieParser());
app.use(express.json());


app.get('/api/artist/search/:name', async (req: Request, res: Response) => {
  console.log('Request received for ID:', req.params['name']);
  
  try {
    const artists = await getSearchArtistResult(req.params['name'].toString());
    if (artists) {
      res.json(artists);
    } else {
      res.status(404).json({ error: 'Name not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' });
  }
});

app.get('/api/artist/similar/:id', async (req: Request, res: Response) => {
  console.log('Request similar received for ID:', req.params['id']);
  
  try {
    const artists = await getSimilarArtists(req.params['id'].toString());
    if (artists) {
      res.json(artists);
    } else {
      res.status(404).json({ error: 'Name not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' });
  }
});



app.get('/api/artist/bio/:link', async (req: Request, res: Response) => {
  console.log('BIO Request received for name:', req.params['link']);
  try {
    const bio = await getBioArtist(`https://api.artsy.net/api/artists/${req.params['link'].toString()}`);
    if (bio) {
      res.json(bio);
    } else {
      res.status(404).json({ error: 'Link not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' });
  }
});

app.post('/api/user/register', async (req: Request, res: Response) => {
  console.log('Register Request received for name:', req.body);
  const token = generateToken(req.body['email']);
  const avatar = getGravatarUrl(req.body['email'])
  try {
    let result = {}
    if (!(await doesEmailExist(req.body['email']))){
    const pass = bcrypt.hashSync(req.body['pass'], saltRounds);
    result = await insert(req.body,avatar,pass,token);
    res.cookie('auth', token, {
      httpOnly: true,
      secure: true,
      sameSite: 'none',
      maxAge: 24 * 60 * 60 * 1000
    });
    }else{
    result = {acknowledged:'exist'}
    }
    if (result) {
      res.json(result);
    } else {
      res.status(404).json({ error: 'Link not found' ,acknowledged:'404'});
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' ,acknowledged:'403'});
  }
});

function generateToken(email: string): string {
  const payload = {
    sub: email,
    iat: Math.floor(Date.now() / 1000)
  };
  return jwt.sign(payload, secretKey, {algorithm: 'HS256', expiresIn: '1h' });
}

app.post('/api/user/login', async (req: Request, res: Response) => {
  const token = generateToken(req.body['email']);
  console.log('Login Request received for name:', req.body);
  try {
    const result = await doesEmailUser(req.body['email'],req.body['pass'],token);
    if (result!==null){
      res.cookie('auth', token, {
        httpOnly: true,
        secure: true,
        sameSite: 'none',
        maxAge: 24 * 60 * 60 * 1000
      });
      res.status(200).send(result);
    } else {
      res.json({acknowledged:'notfound'});
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' ,acknowledged:'403'});
  }
});

app.get('/api/me', async (req, res) => {
  console.log('Cookie Request received for authintication:', req.cookies.auth);
  try {
  const token = req.cookies.auth;
  const user = await findUserByAuth(token);
  if (user && user.acknowledged === 'OK') {
    res.json(user);
  } else {
    res.json({ acknowledged: 'Unauthorized' });
  }
} catch (error) {
  res.json({ 'Error in fetching cookie ': error,acknowledged:'Failed' });
}
});

app.post('/api/user/delete', async (req: Request, res: Response) => {
  console.log('Delete Request received for name:', req.cookies.auth);
  try {
    let result = {}
    result = await deleteOneUser(req.cookies.auth);
    if (result) {
      res.clearCookie('auth');
      res.status(200).json(result);
    } else {
      res.status(404).json({ error: 'Link not found' ,acknowledged:'404'});
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' ,acknowledged:'403'});
  }
});

app.post('/api/user/logout', async (req: Request, res: Response) => {
  console.log('Logout Request received for name:', req.cookies.auth);
  try {
    let result = {}
    result = await logOutOneUser(req.cookies.auth);
    if (result) {
      res.clearCookie('auth');
      res.status(200).json(result);
    } else {
      res.status(404).json({ error: 'Link not found' ,acknowledged:'404'});
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' ,acknowledged:'403'});
  }
});

async function dataEstablish(id: string) {
  const artistDatailCashe: { [key: string]: any } = {};
  const bio = await getBioArtist(`https://api.artsy.net/api/artists/${id.toString()}`);
  artistDatailCashe['artistId'] = id;
  artistDatailCashe['time'] = new Date();
  artistDatailCashe['title'] = bio.name ;
  ;
  if (bio._links.thumbnail) {
    artistDatailCashe['thumbnail'] = bio._links.thumbnail.href;
  }else{
    artistDatailCashe['thumbnail'] = 'artsy_logo.svg';
  }
  artistDatailCashe['nationality']= bio.nationality ;
  if (bio.deathday==='' && bio.birthday==='') {
    artistDatailCashe['dates'] = '';
  }else{
    artistDatailCashe['dates'] = `${bio.birthday ? bio.birthday : ''} - ${bio.deathday ? bio.deathday : 'Present'}`;
  }
  console.log("Artist Data Cached: ", artistDatailCashe);
  return artistDatailCashe;
}

app.post('/api/user/favorites', async (req: Request, res: Response) => {
  console.log('Change Favorites Request received for name:', req.cookies.auth);
  try {
    
    let result = {}
    if(req.body.action === 'add') {
      const bio = await dataEstablish(req.body.id.toString());
      result = await pushOneFavorite(req.cookies.auth, bio);
    } else if (req.body.action === 'remove') {
      result = await popOneFavorite(req.cookies.auth, req.body.id,true);
    } else if (req.body.action === 'retrieve') {
      result = await popOneFavorite(req.cookies.auth, req.body.id,false);
      
    } else {
      res.status(400).json({ error: 'Invalid action specified', acknowledged: '400' });
    }
    if (result) {
      res.json(result);
    } else {
      res.status(404).json({ error: 'Link not found' ,acknowledged:'404'});
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' ,acknowledged:'403'});
  }
});



app.get('/api/artist/artwork/:link', async (req: Request, res: Response) => {
  console.log('Artworks Request received for name:', req.params['link']);
  try {
    const bio = await getBioArtist(`https://api.artsy.net/api/artworks?${req.params['link'].toString()}&size=10`);
    if (bio) {
      res.json(bio);
    } else {
      res.status(404).json({ error: 'Link not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' });
  }
});

app.get('/api/artwork/categories/:id', async (req: Request, res: Response) => {
  console.log('Category Request received for id:', req.params['id']);
  try {
    const cats = await getBioArtist(`https://api.artsy.net/api/genes?artwork_id=${req.params['id'].toString()}`);
    if (cats) {
      res.json(cats);
    } else {
      res.status(404).json({ error: 'Link not found' });
    }
  } catch (error) {
    res.status(500).json({ error: 'Error processing request' });
  }
});

async function getBioArtist(link: string): Promise<any> {
  try {
    const token = await getValidToken();
    
    const response = await axios.get(link, {
      headers: {
        'X-Xapp-Token': token,
        'Accept': 'application/json'
      }
    });    
    return response.data;
    
  } catch (error) {
    console.error('Error fetching artist data:', error);
    throw new Error(`Failed to fetch artist data: ${error}`);
  }
}

async function getArtsyToken(): Promise<string> {
  try {
    const url = 'https://api.artsy.net/api/tokens/xapp_token';
    const clientCodes = {
      client_id: process.env["ARTSY_CLIENT_ID"],
      client_secret: process.env["ARTSY_CLIENT_SECRET"]
    };
    
    const response = await axios.post(url, clientCodes);
    const token = response.data.token;
    
    return token;
  } catch (error) {
    console.error('Error getting Artsy token:', error);
    throw error;
  }
}

let tokenExpiry: number | null = null;
async function getValidToken(): Promise<string> {
  const currentTime = Date.now();
  
  if (!artsyToken || !tokenExpiry || currentTime >= tokenExpiry) {
    artsyToken = await getArtsyToken();
    tokenExpiry = currentTime + (23 * 60 * 60 * 1000);
  }
  
  return artsyToken;
}

async function getSearchArtistResult(name: string): Promise<any> {
  try {
    const endpoint = `https://api.artsy.net/api/search?q=${name}&size=10&type=artist`;
    const token = await getValidToken();
    
    const response = await axios.get(endpoint, {
      headers: {
        'X-Xapp-Token': token,
        'Accept': 'application/json'
      }
    });

    return response.data._embedded.results;
  } catch (error) {
    console.error('Error fetching artist data:', error);
    throw new Error(`Failed to fetch artist data: ${error}`);
  }
}

async function getSimilarArtists(id: string): Promise<any> {
  try {
    const endpoint = `https://api.artsy.net/api/artists?similar_to_artist_id=${id}`;
    const token = await getValidToken();
    const response = await axios.get(endpoint, {
      headers: {
        'X-Xapp-Token': token,
        'Accept': 'application/json'
      }
    });

    return response.data._embedded.artists;
  } catch (error) {
    console.error('Error fetching artist data:', error);
    throw new Error(`Failed to fetch artist data: ${error}`);
  }
}



app.use(express.static(browserDistFolder, {
  maxAge: '1y',
  index: false,
  redirect: false,
}));

app.use('/**', (req, res, next) => {
  angularApp
    .handle(req)
    .then((response) =>
      response ? writeResponseToNodeResponse(response, res) : next(),
    )
    .catch(next);
});


const client = new MongoClient(uri);
                      
 async function mongoRun() {
    try {
         await client.connect();
         const db = client.db("Art");
         const appcol = db.collection("users");
          console.log("Application successfully connected to MongoDB.");
          return appcol
        } catch (err:any) {
          console.error(err.stack);
      }
    }

    async function insert(data:any,avatar :string,pass:string,token:string) {
      
      try{
        const dataToBeInsert = {
            "name": data['name'],
            "email": data['email'],                                                                                                                              
            "password": pass,                                                                                                                                
            "favorites": [],
            "avatar":avatar,
            "auth":token,
            "lastlogin": new Date()
          }
          const db = client.db("Art");
          const appcol = db.collection("users");
          const res = await appcol.insertOne(dataToBeInsert);
          return {acknowledged: 'OK', id: res.insertedId, name: data['name'], avatar: avatar};
       } catch (err:any) {
        console.error(err.stack);
        return {acknowledged: 'Error', error: err.message}; 
    }
    }

    async function doesEmailExist(email: string): Promise<boolean> {
      try {
          const db = client.db("Art");
          const appcol = db.collection("users");
          const user = await appcol.findOne({ 'email': email });
          return user !== null;
      } catch (error) {
          console.error("Error checking email existence:", error);
          return false; 
      }
  }

  

  async function doesEmailUser(email: string,password:string,token:string) {
    try {
        const db = client.db("Art");
        const appcol = db.collection("users");
        const user = await appcol.findOne({ 'email': email});
          if (user && bcrypt.compareSync(password,user.password)){
            appcol.updateOne( { 'email':email} , { $set: { auth : token  , 'lastlogin' : new Date()} } );
            return {acknowledged:'OK', id: user._id , name: user.name, avatar: user.avatar,favorites:user.favorites};
          }else{
            return null;
          } 
    } catch (error) {
        console.error("Error checking email existence:", error);
        return false; 
    }
}

async function findUserByAuth(auth:string) {
  try {
      const db = client.db("Art");
      const appcol = db.collection("users");
      const user = await appcol.findOne({ 'auth': auth});
        if (user){
          return {acknowledged:'OK', id: user._id , name: user.name, avatar: user.avatar,favorites:user.favorites};
        }else{
          return null;
        } 
  } catch (error) {
      console.error("Error checking email existence:", error);
      return false; 
  }
}

async function deleteOneUser(auth: string){
  try {
      const db = client.db("Art");
      const appcol = db.collection("users");
      const user = await appcol.deleteOne({ auth: auth });
      return user;
  } catch (error) {
      console.error("Error checking id existence:", error);
      return false; 
  }
}

async function logOutOneUser(auth: string){
  try {
      const db = client.db("Art");
      const appcol = db.collection("users");
      return await appcol.updateOne( { 'auth':auth} , { $set: {  auth : ""  } } );
  } catch (error) {
      console.error("Error checking id existence:", error);
      return false; 
  }
}

async function pushOneFavorite(auth: string,favproteObject : any) {
  try {
      const db = client.db("Art");
      const appcol = db.collection("users");
      await appcol.updateOne( { 'auth':auth} , { $push: { favorites: favproteObject} } );;
      const fav = await appcol.findOne({ 'auth':auth });
      return fav.favorites
  } catch (error) {
      console.error("Error checking id existence:", error);
      return false; 
  }
}

async function popOneFavorite(auth: string,favproteId : string, removeOne:boolean) {
  try {
      const db = client.db("Art");
      const appcol = db.collection("users");
      if (removeOne){
      await appcol.updateOne( { 'auth':auth} , { $pull: { favorites: {artistId:favproteId}} } );
      }
      const fav = await appcol.findOne({ 'auth':auth });
      console.log("Favorites: ", fav);
      return fav.favorites
  } catch (error) {
      console.error("Error checking id existence:", error);
      return false; 
  }
}


  


  let appcol : any;
if (isMainModule(import.meta.url)) {
  const port = process.env['PORT'] || 4000;
  app.listen(port, () => {
    console.log(`Node Express server listening on http://localhost:${port}`);
    
  });
}
appcol = mongoRun();



export const reqHandler = createNodeRequestHandler(app);
