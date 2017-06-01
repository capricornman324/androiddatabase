package stikombali.ac.id.latihandatabase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Select;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import stikombali.ac.id.latihandatabase.entity.ContactPerson;

public class InputActifity extends AppCompatActivity implements Validator.ValidationListener,DatePickerDialog.OnDateSetListener{

    private static final int REQUES_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private Bitmap bitmap;

    @BindView(R.id.imgFoto)
    ImageView photo;

    @BindView(R.id.txtFName)
    @NotEmpty
    EditText firstname;

    @BindView(R.id.txtLName)
    @NotEmpty
    EditText lastname;

    @BindView(R.id.spinnerSex)
    @Select
    Spinner sex;

    @BindView(R.id.txtDob)
    @NotEmpty
    EditText dob;

    @BindView(R.id.txtAlamat)
    @NotEmpty
    EditText alamat;

    @BindView(R.id.txtPhone)
    @NotEmpty
    EditText phone;

    @BindView(R.id.txtEmail)
    @NotEmpty
    EditText email;

    Validator validator;
    boolean isValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_actifity);

        ButterKnife.bind(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sex.setAdapter(adapter);
        if (shouldAskPermissions()){
            askPermissions();
        }

    }

    protected  boolean shouldAskPermissions(){
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23) //pesan untuk android jiga versi diatas 23
    protected void askPermissions(){
        String [] permissions = {
                "android.permission.CAMERA",
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRIITE_EXTERNAL_STORAGE"
        };
        int requestCode = 200;
        requestPermissions(permissions,requestCode);

    }



    @Override
    public void onValidationSucceeded() {
        isValid = true;
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            isValid = false;
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    protected  boolean validate(){
        if (validator != null){
            validator.validate();
        }
        return isValid;
    }

    @OnClick(R.id.btnSave)
    public void btnClick(){
        if (validate()){
            ContactPerson cp = new ContactPerson();
            cp.setFirstName(firstname.getText().toString().trim());
            cp.setLastName(lastname.getText().toString().trim());
            cp.setAddress(alamat.getText().toString().trim());
            cp.setPhone(phone.getText().toString().trim());
            cp.setEmail(email.getText().toString().trim());
            cp.setSex(sex.getSelectedItem().toString().trim());
            cp.setDob(dob.getText().toString().trim());
            if (bitmap !=null){
                cp.setPhoto(encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,100));
            }else {
                cp.setPhoto("");
            }
            cp.save();

            firstname.setText("");
            lastname.setText("");
            alamat.setText("");
            phone.setText("");
            email.setText("");

            Toast.makeText(this,"Simpan Berhasil", Toast.LENGTH_LONG).show();
            finish();//keluar langsung dari layar

        }
    }

    @OnClick(R.id.btnDob)
    public void btnDobClick(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialogDate = new DatePickerDialog(this,this,year,month,day);

        dialogDate.getDatePicker().setMaxDate(c.getTimeInMillis());
        dialogDate.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dob.setText(year + "-" + (month + 1)+ "-" + dayOfMonth);
    }

    @OnClick(R.id.imgFoto)
    public  void imgPhotoOnClick(){
//        Intent inten = new Intent(); //sebelumnya untuk galery
//        inten.setType("image/*");
//        inten.setAction(Intent.ACTION_GET_CONTENT);
//        inten.addCategory(Intent.CATEGORY_OPENABLE);
//        startActivityForResult(inten,REQUES_CODE);

        PopupMenu popup = new PopupMenu(InputActifity.this,photo);//untuk menu popup
        popup.getMenuInflater().inflate(R.menu.popupmenu,popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(InputActifity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_LONG).show();
                switch (item.getTitle().toString().toUpperCase().trim()){
                    case "GALLERY" :
                        Intent inten = new Intent(); //sebelumnya untuk galery
                        inten.setType("image/*");
                        inten.setAction(Intent.ACTION_GET_CONTENT);
                        inten.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(inten,REQUES_CODE);
                        break;
                    case "CAMERA" :
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent,CAMERA_REQUEST);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popup.show();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        InputStream stream = null;

        if ((requestCode == REQUES_CODE) && (resultCode == Activity.RESULT_OK)){
            try {
                if (bitmap != null){
                    bitmap.recycle();
                }
                stream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
                bitmap = getResizeBitmap(bitmap,400,400);
                photo.setImageBitmap(bitmap);

            }catch(FileNotFoundException e){
                e.printStackTrace();
            }finally {
                if (stream != null){
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if ((requestCode == CAMERA_REQUEST) && (resultCode == RESULT_OK)) {
            if (bitmap != null){
                bitmap.recycle();
            }
            bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = getResizeBitmap(bitmap,400,400);
            photo.setImageBitmap(bitmap);
        }

    }

    public Bitmap getResizeBitmap (Bitmap bm,int newWidth,int newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth  = ((float) newWidth)/width;
        float scaleHeight  = ((float) newHeight)/ height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        Bitmap resizeBitmap = Bitmap.createBitmap(bm,0,0,width,height,matrix,false);
        bm.recycle();
        return resizeBitmap;
    }

    //merubah string jadi bit array
    private String encodeToBase64(Bitmap image,Bitmap.CompressFormat compressFormat, int quality ){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat,quality,byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(),Base64.DEFAULT);
    }
    //selesai

    //menampilkan ke menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ambilgambar,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuAmbilGambar :
                takeImageFromCamera();
                break;
            default:
                break;
        }
        return  true;
    }
    //untuk kamera
    public void takeImageFromCamera(){
        Intent cameraInten = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraInten,CAMERA_REQUEST);
    }


}
