import { Component } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { DataService } from './data-service';
import { ModalComponent } from "../modal/modal.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, ModalComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  login = new FormGroup({
    username: new FormControl(),
    password: new FormControl(),
  });
  feedbackMsg!: string;
  window: Window = window;

  constructor(private dataService: DataService) {}

  onSubmit() {
    const data = this.login.value;
    console.log(data);
    this.dataService.login(data).subscribe(
      (response) => {
        console.log('Response:', response);
        localStorage.setItem('token', response)
        this.feedbackMsg = `Successfully logged in as ${this.login.value.username}`;
        this.showModal('feedback-login')
      },
      (error) => {
        console.error('Error:', error);
        this.feedbackMsg = `Error: ${error.error}`;
        this.showModal('feedback-login')
      }
    );
  }

  showModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('hhidden');
      modal_t.classList.add('sshow');
    }
  }
  closeModal(name: string) {
    let modal_t = document.getElementById(name);
    if (modal_t !== null) {
      modal_t.classList.remove('sshow');
      modal_t.classList.add('hhidden');
    }
  }
}
