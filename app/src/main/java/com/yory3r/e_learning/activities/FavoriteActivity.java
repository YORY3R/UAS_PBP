package com.yory3r.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.FavoriteAdapter;
import com.yory3r.e_learning.adapters.FavoriteAdapter;
import com.yory3r.e_learning.databinding.ActivityAdminBinding;
import com.yory3r.e_learning.databinding.ActivityFavoriteBinding;
import com.yory3r.e_learning.models.FavoriteModel;
import com.yory3r.e_learning.models.UserModel;
import com.yory3r.e_learning.utils.ChangeString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoriteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{

    private FavoriteAdapter adapter;
    private LayoutManager manager;
    private RecyclerView rvFavorite;
    private SearchView svFavorite;
    private SwipeRefreshLayout srFavorite;
    private CardView cvNoData;
    private Button btnPdf;
    private Button btnQr;

    private ArrayList<FavoriteModel> listFavorite;
    private ArrayList<FavoriteModel> favoritePdf;

    private ChangeString change;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    
    private Intent intent;
    private ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_favorite;
        binding = DataBindingUtil.setContentView(FavoriteActivity.this,layout);
        binding.setActivityFavorite(FavoriteActivity.this);

        initFirebase();
        initView();

        listFavorite = new ArrayList<>();

        initListener();
        initAdapter();

        checkEmpty();
    }
    private void initFirebase()
    {
        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Favorite/" + change.DotsToEtc(user.getEmail()));// TODO: 28/11/2021 GANTI JADI DIPISAH
    }

    private void initView()
    {
        rvFavorite = binding.rvFavorite;
        svFavorite = binding.svFavorite;
        srFavorite = binding.srFavorite;
        cvNoData = binding.cvNoData;
        btnPdf = binding.btnPdf;
        btnQr = binding.btnQr;
    }

    private void initListener()
    {
        svFavorite.setOnQueryTextListener(FavoriteActivity.this);
        srFavorite.setOnRefreshListener(FavoriteActivity.this);
        btnPdf.setOnClickListener(FavoriteActivity.this);
        btnQr.setOnClickListener(FavoriteActivity.this);
    }

    public void initAdapter()
    {
        listFavorite = new ArrayList<>();
        favoritePdf = new ArrayList<>();

        for(int a = 0 ; a < 100 ; a++)
        {
            databaseReference.child(String.valueOf(a))
            .addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.getValue() != null)
                    {
                        listFavorite.add(snapshot.getValue(FavoriteModel.class));
                        favoritePdf.add(snapshot.getValue(FavoriteModel.class));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(FavoriteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        adapter = new FavoriteAdapter(FavoriteActivity.this, listFavorite);
        manager = new LinearLayoutManager(FavoriteActivity.this);

        rvFavorite.setLayoutManager(manager);
        rvFavorite.setAdapter(adapter);



    }

    private void checkEmpty()
    {
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task)
            {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        cvNoData.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void onRefresh()
    {
        initAdapter();
        checkEmpty();

        srFavorite.setRefreshing(false);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == btnPdf.getId())
        {
            try
            {
                if(getIntent().getStringExtra("QR_RESULT") == null)
                {
                    Toast.makeText(FavoriteActivity.this, "Scan QR Dulu", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    createPdf();
                }
            }
            catch (FileNotFoundException | DocumentException e)
            {
                e.printStackTrace();
            }
        }
        else if(view.getId() == btnQr.getId())
        {
            gotoQrActivity();
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException
    {
        File folder = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        if(!folder.exists())
        {
            folder.mkdir();
        }

        Date currentTime = Calendar.getInstance().getTime();
        String pdfName = currentTime.getTime() + ".pdf";

        File pdfFile = new File(folder.getAbsolutePath(), pdfName);
        OutputStream outputStream = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, outputStream);
        document.open();

        Paragraph judul = new Paragraph("Daftar Course Favorite \n\n",
                new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD, BaseColor.BLACK));

        judul.setAlignment(Element.ALIGN_CENTER);
        document.add(judul);

        PdfPTable tables = new PdfPTable(new float[]{16, 8});

        tables.getDefaultCell().setFixedHeight(50);
        tables.setTotalWidth(PageSize.A4.getWidth());
        tables.setWidthPercentage(100);
        tables.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        PdfPCell cellSupplier = new PdfPCell();

        cellSupplier.setPaddingLeft(20);
        cellSupplier.setPaddingBottom(10);
        cellSupplier.setBorder(Rectangle.NO_BORDER);

//        UserModel userModel = viewModel.getUserProfileLiveData().getValue();

        Paragraph kepada = new Paragraph("Kepada Yth: \n" + user.getEmail() + "\n",
                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));

        cellSupplier.addElement(kepada);
        tables.addCell(cellSupplier);

        Paragraph NomorTanggal = new Paragraph("No : " + user.getUid() + "\n\n" + "Tanggal : " +
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(currentTime) + "\n",
                new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK));

        NomorTanggal.setPaddingTop(5);
        tables.addCell(NomorTanggal);

        document.add(tables);

        Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
        Paragraph Pembuka = new Paragraph("\nBerikut merupakan daftar Course Favorite: \n\n", font1);
        Pembuka.setIndentationLeft(20);

        document.add(Pembuka);

        PdfPTable tableHeader = new PdfPTable(new float[]{5, 5, 5, 5});
        tableHeader.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        tableHeader.getDefaultCell().setFixedHeight(30);
        tableHeader.setTotalWidth(PageSize.A4.getWidth());
        tableHeader.setWidthPercentage(100);

        PdfPCell h1 = new PdfPCell(new Phrase("No."));
        h1.setHorizontalAlignment(Element.ALIGN_CENTER);
        h1.setPaddingBottom(5);

        PdfPCell h2 = new PdfPCell(new Phrase("Nama"));
        h2.setHorizontalAlignment(Element.ALIGN_CENTER);
        h2.setPaddingBottom(5);

        PdfPCell h3 = new PdfPCell(new Phrase("Kode"));
        h3.setHorizontalAlignment(Element.ALIGN_CENTER);
        h3.setPaddingBottom(5);

        PdfPCell h4 = new PdfPCell(new Phrase("Jurusan"));
        h4.setHorizontalAlignment(Element.ALIGN_CENTER);
        h4.setPaddingBottom(5);

        tableHeader.addCell(h1);
        tableHeader.addCell(h2);
        tableHeader.addCell(h3);
        tableHeader.addCell(h4);

        for(PdfPCell cells : tableHeader.getRow(0).getCells())
        {
            cells.setBackgroundColor(BaseColor.PINK);
        }

        document.add(tableHeader);

        PdfPTable tableData = new PdfPTable(new float[]{5, 5, 5, 5});
        tableData.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        tableData.getDefaultCell().setFixedHeight(30);
        tableData.setTotalWidth(PageSize.A4.getWidth());
        tableData.setWidthPercentage(100);
        tableData.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

        for(int a = 0 ; a < favoritePdf.size() ; a++)
        {
            String no = String.valueOf(a + 1);
            String nama = favoritePdf.get(a).getNama();
            String kode = favoritePdf.get(a).getKode();
            String jurusan = favoritePdf.get(a).getJurusan();

            tableData.addCell(no);
            tableData.addCell(nama);
            tableData.addCell(kode);
            tableData.addCell(jurusan);
        }

        document.add(tableData);

        Font font2 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
        String tglDicetak = currentTime.toLocaleString();

        Paragraph paragraph = new Paragraph("\nDicetak tanggal " + tglDicetak, font2);
        paragraph.setAlignment(Element.ALIGN_RIGHT);

        document.add(paragraph);
        document.close();

        previewPdf(pdfFile);

        Toast.makeText(FavoriteActivity.this, "PDF berhasil dibuat", Toast.LENGTH_SHORT).show();
    }

    private void previewPdf(File pdfFile)
    {
        PackageManager packageManager = getPackageManager();

        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");

        List<ResolveInfo> list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);

        if(list.size() > 0)
        {
            Uri uri;
            uri = FileProvider.getUriForFile(FavoriteActivity.this, getPackageName() + ".provider", pdfFile);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(uri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            grantUriPermission(getPackageName(), uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(pdfIntent);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gotoMainActivity();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(FavoriteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoQrActivity()
    {
        intent = new Intent(FavoriteActivity.this, QrActivity.class);
        startActivity(intent);
        finish();
    }
}