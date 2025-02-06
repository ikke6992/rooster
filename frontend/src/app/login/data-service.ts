import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = `${environment.apiUrl}/v1/users`;

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, data, {
      responseType: 'text',
    });
  }
}
