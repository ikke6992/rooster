import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private apiUrl = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  getData(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }

  getScheduledDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}/scheduleddays/month/${month}/${year}`
    return this.http.get<any>(url);
  }

  getExcel(): Observable<any> {
    return this.http.get(this.apiUrl + `/scheduleddays/export`, { responseType: 'blob' }).pipe(
      map((response: Blob) => {
        saveAs(response, '/scheduleddays.xlsx');
        return response;
      })
    );
  }

  getFreeDaysByMonth(month: number, year: number): Observable<any> {
    const url = `${this.apiUrl}/freedays/month/${month}/${year}`
    return this.http.get<any>(url);
  }

  override(id: number, data: any) {
    const url = `${this.apiUrl}/scheduleddays/override/${id}`;
    return this.http.put<any>(url, data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }

  editNote(id: number, note: any){
    const url = `${this.apiUrl}/lessons/note/${id}`;
    return this.http.put<any>(url, note, { responseType: 'text' as 'json' });
  }

  getGroups(): Observable<any> {
    const url =  `${this.apiUrl}/groups`
    return this.http.get(url);
  }

  getTeachers(): Observable<any> {
    const url =  `${this.apiUrl}/teachers`
    return this.http.get(url);
  }

  addLesson(data: any): Observable<any> {
    return this.http.post<any>(this.apiUrl + '/scheduleddays', data, {
      headers: new HttpHeaders({ 'Content-type': 'application/json' }),
    });
  }
  
  removeLesson(id: number){
    return this.http.delete<any>(`${this.apiUrl}/scheduleddays/${id}`);
  }
} 
