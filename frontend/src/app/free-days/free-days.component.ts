import { Component } from '@angular/core';
import { DataService } from './data-service';
import { CommonModule } from '@angular/common';
import { ModalComponent } from '../modal/modal.component';
import { AddFreeDaysComponent } from './add-free-days/add-free-days.component';

@Component({
  selector: 'app-free-days',
  standalone: true,
  imports: [CommonModule, ModalComponent, AddFreeDaysComponent],
  templateUrl: './free-days.component.html',
  styleUrl: './free-days.component.css'
})
export class FreeDaysComponent {
  freeDays: FreeDays[] = [];
  pastFreeDays: FreeDays[] = [];
  fields: any[] = [];
  showPastDaysCounter: number = 0;
  isLoggedIn: boolean = localStorage.getItem('token') !== null
  
  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getUpcomingDays().subscribe((response: any[]) => {
      this.freeDays = response;
      console.log(response);
    });
  }

  getPastDays(){
    // only request past free days once
    if (this.showPastDaysCounter === 0) {
      this.dataService.getPastDays().subscribe((response: any[]) => {
        this.pastFreeDays= response;
      })
    }
    //counter used to show (even numbers) or close (uneven numbers) past free days
    this.showPastDaysCounter += 1
    
  }

  remove(id: number){  
    this.dataService.removeDay(id).subscribe(() => {
      (this.freeDays.find((freeDay) => freeDay.id === id) || this.pastFreeDays.find((freeday) => freeday.id === id) || this.freeDays[id]).isDeleted = true;
    });    
  }  
  
  showModal() {
    let modal_t = document.getElementById('add-free-days');
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal() {
    let modal_t = document.getElementById('add-free-days');
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}

interface FreeDays {
  id: number;
  name: string;
  date: Date;
  isDeleted: boolean
}