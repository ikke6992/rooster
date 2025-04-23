import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = `${environment.apiUrl}/api/v1`;

  constructor(private http: HttpClient) {}

  getData(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getScheduledDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}/scheduleddays/month/${month}/${year}`;
    return this.http.get<any>(url);
  }

  getExcel(year: number): Observable<any> {
    return this.http
      .get(this.apiUrl + `/scheduleddays/export/${year}`, {
        responseType: 'blob',
      })
      .pipe(
        map((response: Blob) => {
          saveAs(response, 'scheduleddays.xlsx');
          return response;
        })
      );
  }

  getFreeDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}/freedays/month/${month}/${year}`;
    return this.http.get<any>(url);
  }

  override(id: number, data: any) {
    const url = `${this.apiUrl}/scheduleddays/override/${id}`;
    return this.http.put<any>(url, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  editNote(id: number, note: any) {
    const url = `${this.apiUrl}/lessons/note/${id}`;
    return this.http.put<any>(url, note, { responseType: 'text' as 'json' });
  }
}
