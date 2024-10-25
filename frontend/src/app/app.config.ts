import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import {
  HTTP_INTERCEPTORS,
  HttpEvent,
  HttpHandlerFn,
  HttpRequest,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import { routes } from './app.routes';
import { multicast, Observable } from 'rxjs';
import { AuthInterceptor } from './auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([loggingInterceptor])),
    // [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}]
  ],
};

export function loggingInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  // temp hardcoded token
  const token: string = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3Mjk4NTczMDAsImV4cCI6MTcyOTg2MDkwMH0.ovfFo-KWaBJtGQ7ig7Zr78Jumc52dq67bSVLEN7GHiA8_tmbig9S5zqHX2MW7-Vi_-9qwPM-8-JvxBiJY5HsWw"
  if (!token) {
    return next(req);
  }
  
  const req1 = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,  // This is now set as a single string
    },
  });

  console.log(req1);  

  return next(req1);
}

