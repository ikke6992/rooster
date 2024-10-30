import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpHandlerFn,
  HttpInterceptor,
  HttpInterceptorFn,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = 'eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3Mjk4NDgwMzIsImV4cCI6MTcyOTg1MTYzMn0.H7r-vb2AlLNT5FhYg3gHcWtcgHG_by2qJnK-1Banplxa4mXOr8VWdFnLD_LkOe3tb6kTjlqHwsuenGJ1rJE6JQ'

    if (!token) {
      return next.handle(req);
    }

    const req1 = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
    });
    
      console.log(req1.headers.get('Authorization'));
  console.log(token);

    return next.handle(req1);
  }
}

export const tokenInterceptor: HttpInterceptorFn = (
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
) => {
  // temp hardcoded token
  const token: string = 'eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3Mjk4NTM1MjksImV4cCI6MTcyOTg1NzEyOX0.OsXBNLFK4W2bemaWtff_nhfDW0p6312DEf9vQIxTMmN6p5st0KckKN6CiRPc4LCr7ulDFeWyIogvaj8oi19low'
  if (!token) {
    return next(req);
  }
  
  const req1 = req.clone({
    headers: req.headers.set('Authorization', 'Bearer ' + token),
  });

  console.log(req1);

  return next(req1);
}
