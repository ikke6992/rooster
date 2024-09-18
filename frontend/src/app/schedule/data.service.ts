import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = 'http://localhost:8080/api/v1/';

  constructor(private http: HttpClient) {}

  getData(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getScheduledDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}scheduleddays/month/${month}/${year}`
    return this.http.get<any>(url);
  }

  getFreeDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}freedays/month/${month}/${year}`
    return this.http.get<any>(url);
  }

  override(id: number, data: any) {
    const url = `${this.apiUrl}scheduleddays/override/${id}`;
    return this.http.put<any>(url, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }
} 
