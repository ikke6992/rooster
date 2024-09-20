import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';
import { map } from 'rxjs/operators';

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

  getExcel(year: number): Observable<any> {
    return this.http.get(this.apiUrl + `/export/${year}`, { responseType: 'blob' }).pipe(
      map((response: Blob) => {
        saveAs(response, 'scheduleddays.xlsx');
        return response;
      })
    );
  }
} 
