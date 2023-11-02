import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { RegionService } from 'src/app/regions/services/region.service';
import { ClientService } from '../../services/client.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-add-form',
  templateUrl: './add-form.component.html',
  styleUrls: ['./add-form.component.css']
})
export class AddFormComponent {
  regions: Array<any> = [];
  file: File | null = null;
  
  conferencesSubscription!: Subscription;
  addParticipantSubscription!: Subscription;
  constructor(private clientService: ClientService, private fb: FormBuilder, private router: Router, 
    private route: ActivatedRoute, private dialog: MatDialog, private regionService: RegionService){}

  ngOnInit(): void {
    this.conferencesSubscription = this.regionService.getRegions()
    .subscribe((res: any)=>{
      this.regions = res
    })
  }
  
  ClientForm = this.fb.group({
    nom: [, Validators.required],
    email: [, Validators.required],
    phoneNumber: [, Validators.required],
    regionID: [, Validators.required],
    dateNaissance: [, Validators.required]
  })


  AddCl(form: any){
    if(this.ClientForm.status == 'VALID'){
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

      if (emailRegex.test(form.email) && this.file){
          this.addParticipantSubscription = this.clientService.addClient(form.regionID, form)
          .subscribe({
            next:(res: any)=>{
              console.log()
              this.clientService.setClientImage(this.file, res.idClient)
              .subscribe({
                next:(res)=>{
                  this.router.routeReuseStrategy.shouldReuseRoute = () => false;
                  this.router.onSameUrlNavigation = 'reload';
                  this.router.navigate(['/clients'], {relativeTo: this.route});
                  this.dialog.closeAll();
                  Swal.fire({
                    icon: 'success',
                    title: 'Success...',
                    text: 'Client has been added successfully !',
                  })
                }
              })
            },
            error: (res: any)=>{
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Unable to delete this client!',
              })
            }
        }
        )
      }
    }
  }

  onFilechange(event: any) {    
    this.file = event.target.files[0]
  }

  ngOnDestroy() {
    if (this.conferencesSubscription) this.conferencesSubscription.unsubscribe();
    if (this.addParticipantSubscription) this.addParticipantSubscription.unsubscribe();
  }
}
