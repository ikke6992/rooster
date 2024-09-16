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
  fields: any[] = [];
  
  constructor(private dataService: DataService) {}

  ngOnInit(): void {
    this.dataService.getData().subscribe((response: any[]) => {
      this.freeDays = response;
      console.log(response);
    });
  }

  remove(id: number){
    this.dataService.removeDay(id).subscribe();
    (this.freeDays.find((freeDay) => freeDay.id === id) || this.freeDays[id]).isDeleted = true;
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