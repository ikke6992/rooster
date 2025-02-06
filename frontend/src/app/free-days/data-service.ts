import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = `${environment.apiUrl}/v1/freedays`;

  constructor(private http: HttpClient) {}

  getUpcomingDays(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/upcoming`);
  }

  getPastDays(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/past`);
  }

  addDay(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  addMultipleDays(data: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/multi`, data, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    });
  }

  removeDay(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
} 
