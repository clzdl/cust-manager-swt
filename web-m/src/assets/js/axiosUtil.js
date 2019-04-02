import Qs from 'qs';
import axios from 'axios';
const post = ( url , data = {} )=>{
	return new Promise((r, j)=> {
			 axios.post(
					url,
					Qs.stringify(data),
					{
						headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
					},

			).then(resp =>{
					let {flag, data, errorMsg: errMsg, errorCode: code} =  resp.data;
					if (flag === 1) {
						r(data)
					} else {
						j({errMsg, code})
					}
			}).catch(err=>{
				  j({errMsg: err.statusText})
			});
	});
};


const get = ( url , data = {} ) => {
	return new Promise((r, j)=> {
			axios.get(
				 url,
				 {params: data},
				 {
					 headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
				 },

		 ).then(resp =>{
				 let {flag, data, errorMsg: errMsg, errorCode: code} =  resp.data;
				 if (flag === 1) {
					 r(data)
				 } else {
					 j({errMsg, code})
				 }
		 }).catch(err=>{
				 j({errMsg: err.statusText})
		 });
	});
};


export {post,get}
