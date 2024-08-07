import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = 'http://localhost:8080/api/v1/scheduleddays';

  constructor(private http: HttpClient) {}

  getData(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getScheduledDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}/month/${month}/${year}`
    return this.http.get<any>(url);
  }
} 
