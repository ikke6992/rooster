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
// import { AuthInterceptor } from './auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptors([tokenInterceptor])),
  ],
};

export function tokenInterceptor(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn
): Observable<HttpEvent<unknown>> {
  // temp hardcoded token
  const token: string = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9BRE1JTiJ9XSwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MzAyOTI4MzAsImV4cCI6MTczMDI5NjQzMH0.SBq5J8t3J8gQiti8O4DhuwBQtOPlozz4x54txxU296AnSFuiAp3A74dPv-fczQPfa94ddQSqCcif7WNzR3nx9Q"
  if (!token) {
    return next(req);
  }

  const authToken: string = `Bearer ${token}`;
  
  const reqWithToken = req.clone({
    setHeaders: {
      Authorization: authToken,
    },
  });  
  return next(reqWithToken);
}

