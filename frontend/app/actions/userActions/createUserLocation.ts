import axios from "axios";

export async function createLocation(lat:number , lng:number,session:any){
try {
    
    

     await axios.post(`http://localhost:8080/saveUserLocation?lat=${lat}&longtitude=${lng}`,{},
        
            {        headers:{Authorization:`Bearer ${session.value}`}
            ,  withCredentials: true,}      )
      

} catch (error  : any) {
  console.log(error.message);
}

}